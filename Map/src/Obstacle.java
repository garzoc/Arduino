import java.awt.Color;

public class Obstacle extends Panel{
	Obstacle(){
		super();
		this.color=Color.red;
	}
	
	@Override
	public boolean isOpen(){
		return false;	
	}
	
	public String toString(){
		return "Obstacle";
	}
}
