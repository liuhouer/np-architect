package com.imooc.live.seata.tcc.task;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

public interface TaskOne {

    @TwoPhaseBusinessAction(name = "taskOneTxn", commitMethod = "commit", rollbackMethod = "cancel")
    public boolean prepare(BusinessActionContext actionContext,
                           @BusinessActionContextParameter(paramName = "wanted") int wanted);

    public boolean commit(BusinessActionContext actionContext);

    public boolean cancel(BusinessActionContext actionContext);
}
