package edu.Brandeis.cs131.Common.HuTimothy;

import edu.Brandeis.cs131.Common.Abstract.Industry;

public class BasicClient extends MyClient {
	
	public BasicClient(String label, Industry industry){
	super(label, industry);
	}
	
	public String clientType(){
		return "BASIC";
	}
	
	public String toString() {
        return String.format("%s BASIC %s", super.getIndustry(), super.getName());
    }
}
