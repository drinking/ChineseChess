package drinking.android.chess;


public class Role {

	Roler role;
	public Role(){
		role=new NoRole(0, 0);
	}
	public Role(int range_start,int range_end){
		role=new Reder(range_start,range_end);
	}
	public void changeRole(){
		role=role.changeRole();
	}
	public boolean canMove(int range){
		return role.canMove(range);
	}
	public boolean empty(){
		return role instanceof NoRole;
	}
	
}
abstract class Roler{
	//it means red chess range
	int range_start;
	int range_end;
	public Roler(int range_start,int range_end){
		this.range_start=range_start;
		this.range_end=range_end;
	}	
	abstract public Roler changeRole();
	abstract boolean canMove(int range);
}
class NoRole extends Roler{

	public NoRole(int range_start, int range_end) {
		super(range_start, range_end);
	}

	@Override
	public Roler changeRole() {
		return this;
	}

	@Override
	boolean canMove(int range) {
		return false;
	}
	
}
class Reder extends Roler{

	public Reder(int range_start, int range_end) {
		super(range_start, range_end);
	}

	@Override
	boolean canMove(int range) {
		if(range>range_start&&range<range_end){
			return true;
		}
		return false;
	}

	@Override
	public Roler changeRole() {
		return new Blacker(range_start, range_end);
	}
	
}
class Blacker extends Roler{

	public Blacker(int range_start, int range_end) {
		super(range_start, range_end);
	}
	@Override
	boolean canMove(int range) {
		if(range>range_start&&range<range_end){
			return false;
		}
		return true;
	}
	@Override
	public Roler changeRole() {
		return new Reder(range_start, range_end);
	}
	
}