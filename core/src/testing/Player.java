package testing;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Player {

	protected Vector2f position;
	protected Vector4f color;
	
	public Player(float x, float y){
		position = new Vector2f(x, y);
		color = new Vector4f();
	}
	
}
