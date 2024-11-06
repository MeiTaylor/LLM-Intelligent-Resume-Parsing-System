package com.ylw.backend.service;

import com.ylw.backend.dto.InfoForHomeModelClass;

public interface CompanyServiceInterface {
    public InfoForHomeModelClass getInfoForHome(int userId);
}
