package edu.Brandeis.cs131.Common.HuTimothy;

import edu.Brandeis.cs131.Common.Abstract.Industry;

public class SharedClient extends MyClient {
	
	//constructor
	public SharedClient(String label, Industry industry){
	super(label, industry);
	}
	
	//returns client type
	public String clientType(){
		return "SHARED";
	}
	
	//returns string with type
    public String toString() {
        return String.format("%s SHARED %s", super.getIndustry(), super.getName());
    }
}
