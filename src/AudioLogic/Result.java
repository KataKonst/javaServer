package AudioLogic;

public class Result  implements Comparable<Result> {

	
	String name;
	int value;
	Result(String name,int value)
	{
		this.value=value;
		this.name=name;
	}
	@Override
	public int compareTo(Result arg0) {
		return arg0.value-value;
	}
	
	@Override
	public String toString()
	{
		return name+"  "+value;
	}

}
