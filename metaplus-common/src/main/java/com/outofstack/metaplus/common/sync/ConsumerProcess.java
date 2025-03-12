package com.outofstack.metaplus.common.sync;

import com.outofstack.metaplus.common.model.MetaplusPatch;

public interface ConsumerProcess {

    public void doConsumer(MetaplusPatch patch);

}
