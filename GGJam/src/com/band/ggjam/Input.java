package com.band.ggjam;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {
	public class InputStack {
		private class Node {
			int button;
			Node next;
			/** We DON'T want to jump multiple times from one
			 * "up" press, but we still want to be able to use the "up" press when
			 * we are figuring out the direction to use an ability. So, this prevents
			 * a jump from being used multiple times but keeps it in the stack!*/
			boolean usedJump;
			public Node(int button) {
				this.button = button;
				usedJump = false;
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
		
		/** @return True if there's an unused UP in the stack, false otherwise */
		public boolean shouldJump() {
			Node upNode = find(UP);
			if(upNode != null && !upNode.usedJump) {
				upNode.usedJump = true;
				return true;
			}
			return false;
		}
		
		/** @return True if there's an unused DASH in the stack, false otherwise */
		public boolean shouldDash() {
			Node dashNode = find(DASH);
			if(dashNode != null && !dashNode.usedJump) {
				dashNode.usedJump = true;
				return true;
			}
			return false;
		}
		 
		/** @return True if the user is currently holding down the "stomp" action */
		public boolean isStomping() {
			Node stompNode = find(STOMP);
			if(stompNode != null) {
				return true;
			}
			return false;
		}
		
		/** Returns the node containing "button," or null if it's not there */
		private Node find(int button) {
			Node cursor = currentButton;
			while(cursor != null) {
				if(cursor.button == button) return cursor;
				cursor = cursor.next;
			}
			
			return null;
		}
		
		/** Should I walk left, right, or sit perfectly still, contemplating
		 * the meaninglessness of existence? */
		public int walkDirection() {
			Node cursor = currentButton;
			while(cursor != null) {
				if(cursor.button == LEFT)
					return -1;
				if(cursor.button == RIGHT)
					return 1;
				cursor = cursor.next;
			}
			
			return 0;
		}
	}
	
	// Static key values
	// Directions are defined as follows:
	//
	//  7 0 1
	//  6 X 2
	//  5 4 3
	//
	public static final int UP = 0;
	public static final int RIGHT = 2;
	public static final int DOWN = 4;
	public static final int LEFT = 6;
	public static final int ACTION = 8;
	public static final int RUN = 9;
	public static final int DASH = 10;
	public static final int GLIDE = 11;
	public static final int STOMP = 12;
	
	// Color keys (AHAHA ES FUNNY JOKE...BECAUSE...COLORKEY...HA)
	public static final int RED = 1;
	public static final int BLUE = 2;
	public static final int GREEN = 4;
	
	
    /** Defines the direction to perform an action if the user has nothing held down. */
	public final static int DEFAULT_DIRECTION = UP;
	
	/** A reasonable measurement for how long someone might have let go of one
	 * key but been meaning to let go of it WITH another key. For example if you
	 * wanted to press QW at the same time, you let go of them within ~20ms
	 */
	private static final long REASONABLE_KEYRELEASE_DELAY = 50;
	
	// Button arrays
	public boolean[] buttons = new boolean[32];
	public boolean[] oldButtons = new boolean[32];
	
	public InputStack buttonStack;
	
	private long[] colorDelays;
	private int currentColor;
	public int colorChoice;

	public Input() {
		super();
		buttonStack = new InputStack();
		
		colorDelays = new long[8];
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
		else if (key == Keys.Z)          button = DASH;
		//if (key == Keys.Z) 			button = ACTION;
		else if (key == Keys.S)          button = GLIDE;
		else if (key == Keys.SHIFT_LEFT) button = STOMP;
		
		// Handle colors differently, since they're stored in a different array
		else if (key == Keys.Q)          setColor(RED, down);
		else if (key == Keys.W)          setColor(BLUE, down);
		else if (key == Keys.E)          setColor(GREEN, down);
		
		// If it's recognized, set the state in the array
		if(button >= 0) {
			if(down) {
				buttonStack.push(button);
			}
			else {
				buttonStack.delete(button);
			}
		}
		
		//Thomas: Running is a special case.  We just want to toggle running based on
		//Elliot: Did you forget about this comment halfway through?                 ^^^^
		//Guess so. DERP
		
		
	}
	
	private void setColor(int color, boolean pressed) {
		if(pressed)
			currentColor |= color;
		else {
			currentColor ^= color;
			colorDelays[color - 1] = System.currentTimeMillis();
		}
		
		if(currentColor == 0) {
			colorChoice = 0;
			long endTime = System.currentTimeMillis();
			for(int i = 1; i <= colorDelays.length; i ++) {
				if(endTime - colorDelays[i - 1] < REASONABLE_KEYRELEASE_DELAY)
					colorChoice += i;
			}
		}
	}
	
	public int getNewColor() {
		return colorChoice;
	}
	
	/**
	 * Call this after you use the information from getNewColor(),
	 * to free colorChoice for the next change
	 */
	public void freeNewColor() {
		colorChoice = 0;
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
		// TODO Auto-generated method stub
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
