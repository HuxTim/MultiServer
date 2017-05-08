package edu.Brandeis.cs131.Common.HuTimothy;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Industry;

public abstract class MyClient extends Client {
    public MyClient(String label, Industry industry) {
        super(label, industry, (int)(Math.random()*10), 3);
    }
    
    
    public abstract String clientType();
    

    }

