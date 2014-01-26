package com.band.ggjam;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {
	public static boolean clicked = false;
	
	public class InputStack {
		private class Node {
			int button;
			Node next;
			/** We DON'T want to jump multiple times from one
			 * "up" press, but we still want to be able to use the "up" press when
			 * we are figuring out the direction to use an ability. So, this prevents
			 * a jump from being used multiple times but keeps it in the stack!*/
			boolean usedNode;
			public Node(int button) {
				this.button = button;
				usedNode = false;
			}
		}
		
		private Node currentButton;
		
		public void push(int button) {
			Node next = currentButton;
			currentButton = new Node(button);
			currentButton.next = next;
		}
		
		public int delete(int button) {
			if(currentButton == null) return -1;
			
			if(currentButton.button == button) {
				currentButton = currentButton.next;
				return button;
			}
			
			Node cursor = currentButton;
			while(cursor.next != null) {
				if(cursor.next.button == button) {
					cursor.next = cursor.next.next;
					return button;
				}
				cursor = cursor.next;
			}
			return -1;
		}
		
		public int peek() {
			return (currentButton == null) ? -1 : currentButton.button;
		}
		
		/** Returns the "dominant direction." That is, the current direction the user would expect
		 * to go if they pressed "dash" while holding the directions they are now. If the user is pressing
		 * no direction keys, default to DEFAULT_DIRECTION (below).  If the user is pressing two to four
		 * opposing directions, use the one they pressed LAST. */
		public Point airDirection() {
			Node cursor = currentButton;
			
			Point result = new Point(0, 0);
			Point lastPressed = null;
			
			while(cursor != null) {
				if(cursor.button <= LEFT) {
					Point pressedDir = Utility.offsetFromDirection(cursor.button);
					result.addPoint(pressedDir);
					
					if(lastPressed == null)
						lastPressed = pressedDir;
				}
			
				cursor = cursor.next;
			}
			
			// There was a tie! Either no buttons are being pressed, or 2-4 are conflicting.
			if(result.equals(new Point(0, 0))) {
				// No buttons
				if(lastPressed == null)
					return Utility.offsetFromDirection(DEFAULT_DIRECTION);
				
				// 2-4 buttons
				return lastPressed;
			}
			
			// Everything is normal. Do not worry.
			return result;
		}
		
		/** @return True if there's an unused UP in the stack, false otherwise */
		public boolean shouldJump() {
			Node upNode = find(UP);
			if(upNode != null && !upNode.usedNode) {
				upNode.usedNode = true;
				return true;
			}
			return false;
		}
		
		/** @return True if there's an unused SPACE (switch) in the stack, false otherwise */
		public boolean shouldSwitch() {
			Node switchNode = find(ACTION);
			if(switchNode != null && !switchNode.usedNode) {
				switchNode.usedNode = true;
				return true;
			}
			return false;
		}
		 
		/** @return True if the user is currently holding down an "action" button */
		public boolean isHoldingActionKey() {
			Node actionNode = find(ACTION);
			if(actionNode != null) {
				return true;
			}
			return false;
		}
		
		/** Returns the node containing "button," or null if it's not there */
		public Node find(int button) {
			Node cursor = currentButton;
			while(cursor != null) {
				if(cursor.button == button) return cursor;
				cursor = cursor.next;
			}
			
			return null;
		}
		
		/**
		 * Looks for the most recent direction then SWALLOWS ITS SOOOUL.
		 * So it can't be used again, that is. Useful for the current debug
		 * "turn based" movement
		 * @return Which direction it would be... wise... to go
		 */
		public Point walkDirection() {
			Node cursor = currentButton;
			while (cursor != null) {
				if (cursor.button == UP || cursor.button == LEFT ||
						cursor.button == DOWN || cursor.button == RIGHT) {
					return Utility.offsetFromDirection(cursor.button);
				}	
				cursor = cursor.next;
			}
			return Utility.offsetFromDirection(NO_DIRECTION);
		}
		
		public Point waveWalkDirection() {
			Node cursor = currentButton;
			while (cursor != null) {
				if (cursor.button == WAVE_UP || cursor.button == WAVE_LEFT ||
						cursor.button == WAVE_DOWN || cursor.button == WAVE_RIGHT) {
					return Utility.offsetFromDirection(cursor.button - 20);
				}	
				cursor = cursor.next;
			}
			return Utility.offsetFromDirection(NO_DIRECTION);
		}
	}
	
	// Static key values
	// Directions are defined as follows:
	//
	//  7 0 1
	//  6 X 2
	//  5 4 3
	//
	public static final int NO_DIRECTION = -1;
	public static final int UP = 0;
	public static final int RIGHT = 2;
	public static final int DOWN = 4;
	public static final int LEFT = 6;
	
	public static final int RESTART = 10;
	public static final int ACTION = 12;
	
	public static final int WAVE_UP = UP + 20;
	public static final int WAVE_DOWN = DOWN + 20;
	public static final int WAVE_LEFT = LEFT + 20;
	public static final int WAVE_RIGHT = RIGHT + 20;
	
	
    /** Defines the direction to perform an action if the user has nothing held down. */
	public final static int DEFAULT_DIRECTION = UP;
	
	// Button arrays
	public boolean[] buttons = new boolean[32];
	public boolean[] oldButtons = new boolean[32];
	
	public InputStack buttonStack;

	public Input() {
		super();
		buttonStack = new InputStack();
	}
	
	/**
	 * Sets button in the array to state of the key
	 * 
	 * @param key
	 * @param down
	 */
	public void set(int key, boolean down) {
		//buttons = new boolean[32];
		// Defaults to nothing pressed in case it's not a recognized keystroke
		int button = -1;
		
		// Go through key possibilities for recognized input
		if (key == Keys.DPAD_UP)    button = UP;
		else if (key == Keys.DPAD_DOWN)  button = DOWN;
		else if (key == Keys.DPAD_LEFT)  button = LEFT;
		else if (key == Keys.DPAD_RIGHT) button = RIGHT;
		else if (key == Keys.W)  button = WAVE_UP;
		else if (key == Keys.A)  button = WAVE_LEFT;
		else if (key == Keys.S)  button = WAVE_DOWN;
		else if (key == Keys.D)  button  = WAVE_RIGHT;
		
		else if (key == Keys.R)          button = RESTART;
		else if (key == Keys.SPACE)		 button = ACTION;
		
		// If it's recognized, set the state in the array
		if(button >= 0) {
			if(down) {
				buttonStack.push(button);
			}
			else {
				buttonStack.delete(button);
			}
		}
	}
	
	/**
	 * Set the button inputs to the past
	 */
	public void tick() {
		for (int i = 0; i < buttons.length; i++)
			oldButtons[i] = buttons[i];
	}
	
	public void releaseAllKeys() {
		buttons = new boolean[32];
	}
	
	public boolean keyDown(int keycode) {
		clicked = true;
		
		// Bind escape to close the game
		if(keycode == Keys.ESCAPE) {
			Gdx.app.exit();
		}
		
		set(keycode, true);
		return false;
	}

	public boolean keyUp(int keycode) {
		set(keycode, false);
		return false;
	}
	
	/**
	 * Not useful right now but I gotta implement it
	 */
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		clicked = true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
