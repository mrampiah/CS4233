package engine;

import gettysburg.common.GbgGame;

public class Factory {

	public static GbgGame makeGame()
	{
		return new GameEngine();
	}

	public static TestGameEngine makeTestGame(){
	    return new TestGameEngine();
    }
}
