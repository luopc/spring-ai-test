package com.luopc.platform.cloud.service.service;

import com.luopc.platform.cloud.service.mode.ClientInfo;

public interface ClientService {
    ClientInfo getClientInfo(String clientId);
}
