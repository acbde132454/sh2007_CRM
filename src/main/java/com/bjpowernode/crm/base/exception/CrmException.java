package com.bjpowernode.crm.base.exception;

import com.bjpowernode.crm.base.constans.CrmEnum;

public class CrmException extends RuntimeException {

        public CrmException(CrmEnum crmEnum) {
            super(crmEnum.getMessage());
        }
}
