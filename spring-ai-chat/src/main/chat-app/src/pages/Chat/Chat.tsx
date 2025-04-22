import { useState, useRef, useEffect } from 'react';
import './Chat.css';
import {Env} from "../Env.ts";

interface Message {
    id: string;
    content: string;
    role: 'user' | 'assistant';
    timestamp: number;
    isComplete?: boolean;
}

const ChatPage = () => {
    const [inputMessage, setInputMessage] = useState('');
    const [messages, setMessages] = useState<Message[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [userId, setUserId] = useState<string>('');
    const bottomRef = useRef<HTMLDivElement>(null);
    const controllerRef = useRef<AbortController | null>(null);

    // 初始化生成用户ID
    useEffect(() => {
        setUserId(crypto.randomUUID());
    }, []);

    // 自动滚动处理
    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const generateId = () => Date.now().toString(36) + Math.random().toString(36).substr(2);

    // 字符类型检测
    const getCharType = (char: string): 'chinese' | 'english' | 'other' => {
        if (/[\u4e00-\u9fa5\u3000-\u303F\uFF00-\uFFEF]/.test(char)) {
            return 'chinese'
        }
        if (/[a-zA-Z]/.test(char)) {
            return 'english'
        }
        return 'other'
    }

    // 智能空格处理核心逻辑
    const processContent = (prev: string, newData: string): string => {
        if (prev.length === 0) return newData

        const lastChar = prev.slice(-1)
        const newFirstChar = newData[0] || ''

        const prevType = getCharType(lastChar)
        const newType = getCharType(newFirstChar)

        let processed = newData

        // 需要添加空格的情况
        const shouldAddSpace =
            (prevType === 'english' && newType === 'english') || // 英文接英文
            (prevType === 'chinese' && newType === 'english') || // 中文接英文
            (prevType === 'english' && newType === 'chinese' && !/[!?,.]$/.test(lastChar)) // 英文接中文（非标点结尾）

        // 需要删除空格的情况
        const shouldRemoveSpace =
            (prevType === 'chinese' && newType === 'chinese') || // 中文接中文
            (prevType === 'other' && /^[\u4e00-\u9fa5]/.test(newData)) // 特殊符号接中文

        if (shouldAddSpace && !lastChar.match(/\s/) && !newFirstChar.match(/\s/)) {
            processed = ' ' + processed
        } else if (shouldRemoveSpace) {
            processed = processed.replace(/^\s+/, '')
        }

        return processed
    }

    // 解析SSE流数据
    const parseSSE = async (reader: ReadableStreamDefaultReader<Uint8Array>, assistantId: string) => {
        const decoder = new TextDecoder();
        let buffer = '';

        try {
            while (true) {
                const { done, value } = await reader.read();
                if (done) break;

                buffer += decoder.decode(value, { stream: true });

                // 处理完整的事件块
                while (buffer.includes('\n\n')) {
                    const eventEndIndex = buffer.indexOf('\n\n');
                    const eventData = buffer.slice(0, eventEndIndex);
                    buffer = buffer.slice(eventEndIndex + 2);

                    // 解析事件字段
                    let messageData = '';
                    eventData.split('\n').forEach(line => {
                        if (line.startsWith('data:')) {
                            messageData += line.replace(/^data:\s*/, '');
                        }
                    });

                    // 处理DONE标记
                    if (messageData === '[DONE]') {
                        setMessages(prev => prev.map(msg =>
                            msg.id === assistantId ? { ...msg, isComplete: true } : msg
                        ));
                        return;
                    }

                    // 更新消息内容
                    if (messageData) {
                        setMessages(prev => prev.map(msg => {
                            if (msg.id === assistantId) {
                                console.log('Updating assistant message:', processContent(msg.content, messageData));
                                return {
                                    ...msg,
                                    content: msg.content + processContent(msg.content, messageData)
                                };
                            }
                            return msg;
                        }));
                    }
                }
            }
        } catch (error) {
            console.error('Stream reading error:', error);
        }
    };

    const handleSubmit = async (e?: React.FormEvent) => {
        e?.preventDefault();
        if (!inputMessage.trim() || isLoading || !userId) return;

        // 用户消息
        const userMessage: Message = {
            id: generateId(),
            content: inputMessage,
            role: 'user',
            timestamp: Date.now(),
        };

        // 助手消息占位
        const assistantMessage: Message = {
            id: generateId(),
            content: '',
            role: 'assistant',
            timestamp: Date.now(),
            isComplete: false
        };

        setMessages(prev => [...prev, userMessage, assistantMessage]);
        setInputMessage('');
        setIsLoading(true);

        try {
            controllerRef.current = new AbortController();

            const response = await fetch(`${Env.API_BASE_URL}/stream`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId, message: inputMessage }),
                signal: controllerRef.current.signal
            });

            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            if (!response.body) throw new Error('No response body');

            await parseSSE(response.body.getReader(), assistantMessage.id);

        } catch (error) {
            if ((error as Error).name !== 'AbortError') {
                setMessages(prev => [...prev, {
                    id: generateId(),
                    content: '请求失败，请重试',
                    role: 'assistant',
                    timestamp: Date.now(),
                    isComplete: true
                }]);
            }
        } finally {
            setIsLoading(false);
            controllerRef.current = null;
        }
    };

    // 停止生成处理（保持不变）
    const handleStop = () => {
        controllerRef.current?.abort();
        setIsLoading(false);
    };

    // JSX保持不变
    return (
        <div className="chat-container">
            <div className="messages-container">
                {messages.map((message) => (
                    <div
                        key={message.id}
                        className={`message ${message.role}`}
                    >
                        <div className="message-content">
                            {message.content}
                            {!message.isComplete && message.role === 'assistant' && (
                                <span className="typing-indicator">...</span>
                            )}
                        </div>
                        <div className="message-time">
                            {new Date(message.timestamp).toLocaleTimeString()}
                        </div>
                    </div>
                ))}
                <div ref={bottomRef} />
            </div>

            <form onSubmit={handleSubmit} className="input-area">
                <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    placeholder="输入消息..."
                    disabled={isLoading}
                    autoFocus
                />
                <button
                    type="submit"
                    disabled={isLoading}
                >
                    {isLoading ? '发送中...' : '发送'}
                </button>
                {isLoading && (
                    <button
                        type="button"
                        onClick={handleStop}
                        className="stop-button"
                    >
                        停止
                    </button>
                )}
            </form>
        </div>
    );
};

export default ChatPage;