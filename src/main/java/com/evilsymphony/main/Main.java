package com.evilsymphony.main;

import com.evilsymphony.controller.Game;

import java.io.IOException;

class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Game game = new Game();
        game.run();
    }


}
