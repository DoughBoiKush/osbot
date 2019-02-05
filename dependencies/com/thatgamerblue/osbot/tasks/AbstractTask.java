package com.thatgamerblue.osbot.tasks;

import com.thatgamerblue.osbot.framework.BaseScript;
import org.osbot.rs07.script.MethodProvider;

public abstract class AbstractTask extends MethodProvider {

    protected final BaseScript mP;

    public AbstractTask(BaseScript methodProvider) {
        mP = methodProvider;
        this.exchangeContext(mP.bot);
    }

    public abstract boolean validate();

    public abstract void execute() throws InterruptedException;

}
