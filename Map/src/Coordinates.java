
public class Coordinates {
	private int x;
	private int y;
	private float xF;
	private float yF;
	Coordinates(int x,int y){
		this.x=x;
		this.y=y;
		this.xF=x;
		this.yF=y;
	}
	
	Coordinates(float x,float y){
		this.x=(int)x;
		this.y=(int)y;
		this.xF=x;
		this.yF=y;
	}
	
	public void alter(int x,int y){
		this.x=x;
		this.y=y;
		this.xF=x;
		this.yF=y;
	}
	
	public void alterf(float x,float y){
		this.xF=x;
		this.yF=y;
		this.x=(int)x;
		this.y=(int)y;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public float xF(){
		return this.xF;
	}
	
	public float yF(){
		return this.yF;
	}
	
}
