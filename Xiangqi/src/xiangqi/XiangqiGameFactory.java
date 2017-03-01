/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/

package xiangqi;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import xiangqi.common.XiangqiGame;
import xiangqi.common.XiangqiGameVersion;
import xiangqi.studentekampiah.versions.alpha.AlphaXiangqiGame;
import xiangqi.studentekampiah.versions.beta.BetaXiangqiGame;

/**
 * A simple factory object that creates the appropriate instance of a XiangqiGame.
 * @version Dec 26, 2016
 */
public class XiangqiGameFactory
{
	/**
	 * Factory method that returns an instance of the requested game.
	 * @param version the version requested
	 * @return the instance of the requested game
	 */
	public static XiangqiGame makeXiangqiGame(XiangqiGameVersion version)
	{
		switch (version){
			case ALPHA_XQ:
				return new AlphaXiangqiGame();
			case BETA_XQ:
				return BetaXiangqiGame.makeBetaXiangqiGame();
			case GAMMA_XQ:
				break;
			case DELTA_XQ:
		}

		throw new NotImplementedException();
	}
}
