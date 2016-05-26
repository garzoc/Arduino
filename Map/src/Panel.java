import java.awt.Color;

public class Panel {
	protected Color color;
	
	Panel(){
	 this.color=Color.green;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public boolean isOpen(){
		return true;
	}
	
	public String toString(){
		return "accesible area";
	}
	
	
}
