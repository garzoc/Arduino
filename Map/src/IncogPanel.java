import java.awt.Color;

public class IncogPanel extends Panel{
	IncogPanel(){
		super();
		this.color=Color.gray;
	}
	
	@Override
	public boolean isOpen(){
		return false;	
	}
	
	public String toString(){
		return "unkonwn area";
	}
}
