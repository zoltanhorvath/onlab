package com.settlerofcatan.websocketconnectionmanager.actor;

import akka.actor.AbstractActor;

public class AppUser extends AbstractActor {

    private String email;


    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
