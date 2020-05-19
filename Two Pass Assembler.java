import java.util.*;
import java.io.*;

class two{
	static Vector<String> Symbols = new Vector<String>();
	static String[] psuedoOp = new String[]{"USING","DROP","START","EQU","DC","DS"};
	static String[] twoByteOp = new String[]{"SR","BR","AR","LR"};
    static String[] fourByteOp = new String[]{"L","ST","BNE","LA","A","C"};

	static LinkedHashMap<String,Details> st = new LinkedHashMap<>();
	static LinkedHashMap<String,Details> lt = new LinkedHashMap<>();
	static LinkedHashMap<String,Integer> bt = new LinkedHashMap<>();
	static LinkedHashMap<Integer,Values> im = new LinkedHashMap<>();	
	static LinkedHashMap<String,String> dsdc = new LinkedHashMap<>();	
	static int content,register;

	public static void display(){
		System.out.println("\n\tSYMBOL TABLE");
		for(Map.Entry<String,Details> entry: st.entrySet()){
			String sym = entry.getKey();
			Details temp = entry.getValue();
			System.out.println(sym+" \t"+temp.value+" \t"+temp.length+" \t"+temp.addressing);
		}
	}
	public static void displayLT(){
		System.out.println("\n\tLITERAL TABLE");
		for(Map.Entry<String,Details> entry: lt.entrySet()){
			String sym = entry.getKey();
			Details temp = entry.getValue();
			System.out.println(sym+" \t"+temp.value+" \t"+temp.length+" \t"+temp.addressing);
		}
	}

	public static void displayIC(int i){
		int stc=0,ltc=0;
		if(i==0)
		System.out.println("\n\tINTERMEDIATE CODE");
		else
			System.out.println("\n\tPASS TWO CODE");
		for(Map.Entry<Integer,Values> entry: im.entrySet()){
			Integer sym = entry.getKey();
			Values temp = entry.getValue();
			if(!temp.instruction.equals("USING"))
			{
				String te[] = temp.vals.split(",");
				// System.out.println(sym+" \t"+temp.instruction+" \t"+temp.vals);
				if(te.length>=2){
					if(st.containsKey(te[0])){stc=0;
					for(Map.Entry<String,Details> entr: st.entrySet()){stc++;
						if(entr.getKey().equals(te[0])){
							te[0]="#ST"+stc;
							break;
						}
					}
				}

				if(te[1].contains("(")&&!te[1].contains("=")){
					int t = te[1].indexOf("(");
					String one = te[1].substring(0,t);
					String two = te[1].substring(t+1,te[1].length()-1);
					stc=0;
					for(Map.Entry<String,Details> entr: st.entrySet()){stc++;
						if(entr.getKey().equals(one)){
							te[1]="#ST"+stc;
							break;
						}
					}
					for(Map.Entry<String,Details> entr: st.entrySet()){stc++;
						if(entr.getKey().equals(two)){
							te[1]+="#ST"+stc;
							break;
						}
					}
					
				}
				else if(st.containsKey(te[1])){
					stc=0;
					for(Map.Entry<String,Details> entr: st.entrySet()){stc++;
						if(entr.getKey().equals(te[1])){
							te[1]="#ST"+stc;
							break;
						}
					}
				}
				else if(lt.containsKey(te[1].substring(1))){
					ltc=0;
					for(Map.Entry<String,Details> entr: lt.entrySet()){ltc++;
						if(entr.getKey().equals(te[1].substring(1))){
							te[1]="#LT"+ltc;
							break;
						}
					}

				}
				System.out.println(sym+"\t"+temp.instruction+"\t"+te[0]+","+te[1]);
						
			}
			else{
				String k = temp.vals;
				if(st.containsKey(k)){
					stc=0;
					for(Map.Entry<String,Details> entr: st.entrySet()){stc++;
						if(entr.getKey().equals(k)){
							k="#ST"+stc;
							break;
						}
					}
				}
				System.out.println(sym+"\t"+temp.instruction+"\t"+k);

			}
			}
			
		}
	}

	public static void passTwo(){
		for(Map.Entry<Integer,Values> entryy: im.entrySet()){
			Integer loc = entryy.getKey();
			Values values = entryy.getValue();
			String temp[] = values.vals.split(",");
			
			if(temp.length >= 2){
				//replace word for value from sym table
				if(st.containsKey(temp[0])){
					Details det = st.get(temp[0]);
					temp[0]=String.valueOf(det.value);
				}

				if(temp[1].contains("(")&&!temp[1].contains("=")){
					int t = temp[1].indexOf("(");
					String one = temp[1].substring(0,t);
					String two = temp[1].substring(t+1,temp[1].length()-1);
					Details de1 = st.get(one);
					Details de2 = st.get(two);
					int diff=10000;
					String min="",basee="-";
					for(Map.Entry<String,Integer> entry: bt.entrySet()){
						 basee = entry.getKey().split(",")[0];
						int lo = Integer.parseInt(entry.getKey().split(",")[1]);
						int vall = entry.getValue();
						if(loc>=lo&&Math.abs(vall-de1.value)<diff){
							diff = Math.abs(vall-de1.value);
							min=basee;
						}				
					}
					
					
					values.vals=temp[0]+","+diff+"("+de2.value+","+min+")";
					System.out.println(loc+"\t"+values.instruction+"\t"+values.vals);
					im.put(loc,values);
				}
				else if(st.containsKey(temp[1])&& !values.instruction.equals("USING")){
					Details det = st.get(temp[1]);
					if(!Arrays.asList(twoByteOp).contains(values.instruction))
					{
						int diff=10000;
						String min="",basee="-";
						for(Map.Entry<String,Integer> entry: bt.entrySet()){
							 basee = entry.getKey().split(",")[0];
							int lo = Integer.parseInt(entry.getKey().split(",")[1]);
							int vall = entry.getValue();
							if(loc>=lo&&Math.abs(vall-det.value)<diff){
								diff = Math.abs(vall-det.value);
								min=basee;
							}				
					}



						values.vals=temp[0]+","+diff+"("+0+","+min+")";

					}
					else
						values.vals=temp[0]+","+det.value;
					System.out.println(loc+"\t"+values.instruction+"\t"+values.vals);

					im.put(loc,values);
				}
				else if(lt.containsKey(temp[1].substring(1))){
					Details det = lt.get(temp[1].substring(1));
					if(!Arrays.asList(twoByteOp).contains(values.instruction))
					{
						int diff=10000;
						String min="",basee="-";
						for(Map.Entry<String,Integer> entry: bt.entrySet()){
							 basee = entry.getKey().split(",")[0];
							int lo = Integer.parseInt(entry.getKey().split(",")[1]);
							int vall = entry.getValue();
							if(loc>=lo&&Math.abs(vall-det.value)<diff){
								diff = Math.abs(vall-det.value);
								min=basee;
							}				
						}

						values.vals=temp[0]+","+diff+"("+0+","+min+")";}
					else
						values.vals=temp[0]+","+det.value;
					System.out.println(loc+"\t"+values.instruction+"\t"+values.vals);

				}
				if(values.instruction.equals("USING")){
							loc+=1;
					if(st.containsKey(temp[1])){
							Details det = st.get(temp[1]);
							register= det.value;
					}else
						register = Integer.parseInt(temp[1]);
					if(temp[0].equals("*")){
						content=loc;
					}
					else if(temp[0].contains("+")){
						String te[] = temp[0].split("\\+");
						int ff=0;
						if(st.containsKey(te[1])){
							Details de = st.get(te[1]);
							ff=de.value;
						}
						else{
							ff=Integer.parseInt(te[1]);
						}
						content=loc+ff;
					}
					else{
						if(st.containsKey(temp[0])){
							Details det = st.get(temp[0]);
							content= det.value;
						}else{
							content = Integer.parseInt(temp[0]);
						}
					}
				bt.put(register+","+loc,content);

					
				}
			}					

			else{
				
				if(values.instruction.equals("BNE")){
					Details det = st.get(temp[0]);
					values.instruction = "BC";
					int diff=10000;
						String min="",basee="-";
						for(Map.Entry<String,Integer> entry: bt.entrySet()){
							 basee = entry.getKey().split(",")[0];
							int lo = Integer.parseInt(entry.getKey().split(",")[1]);
							int vall = entry.getValue();
							if(loc>=lo&&Math.abs(vall-det.value)<diff){
								diff = Math.abs(vall-det.value);
								min=basee;
							}				
					}
					System.out.println(loc+"\t"+values.instruction+"\t"+"7,"+diff+"("+0+","+min+")");
					values.vals = "7,"+diff+"("+0+","+min+")";//register 7 stor
					im.put(loc,values);
				}

				if(values.instruction.equals("BR")){
					
					values.instruction = "BCR";					
					System.out.println(loc+"\t"+values.instruction+"\t"+"15,14");
					values.vals = "15,14";//register 7 stor
					im.put(loc,values);
				}

			}

		}
		int loc=0;
		for(Map.Entry<String,Details> entry: lt.entrySet()){
			String sym = entry.getKey();
			String s;
			Details temp = entry.getValue();
			loc=temp.value;
			if(st.get(sym.substring(2,sym.length()-1))!=null){
				Details tep = st.get(sym.substring(2,sym.length()-1));
				s = Integer.toHexString(tep.value);
			}else
			s = Integer.toHexString(Integer.parseInt(sym.substring(2,sym.length()-1)));
			int t = 8-s.length();
			for(int i=0;i<=t;i++){
				s = "0"+s;
			}
			s="X'"+s+"'";
			System.out.println(loc+"\t"+s);
			im.put(loc,new Values(s,""));

		}
		for(Map.Entry<String,Details> entry: st.entrySet()){
			String sym = entry.getKey();
			String s;
			Details temp = entry.getValue();
			if(temp.value<loc)
				continue;
			 loc=temp.value;
			String ss = dsdc.get(sym);
			if(ss==null){
				System.out.println(loc);
				im.put(loc,new Values("",""));
				continue;
			}
			if(ss.contains(","))
				continue;

			s = Integer.toHexString(Integer.parseInt(ss));
			int t = 8-s.length();
			for(int i=0;i<=t;i++){
				s = "0"+s;
			}
			s="X'"+s+"'";
			System.out.println(loc+"\t"+s);
			im.put(loc,new Values(s,""));

		}
	}

	public static void displayBaseTable(){
	System.out.println("\n\tBASE TABLE\nRegister Content");
		for(Map.Entry<String,Integer> entry: bt.entrySet()){
			String a = entry.getKey();
			int b = entry.getValue();
			System.out.println(a.split(",")[0]+" \t"+b);
		}
	}


	public static int pool(int loc){
		
		for(Map.Entry<String,Details> entry: lt.entrySet()){
			String sym = entry.getKey();
			Details temp = entry.getValue();
			if(temp.length==4)
			{lt.put(sym,new Details(loc,4,"R"));
			loc = loc + 4;}
			else if(temp.length==2)
			{lt.put(sym,new Details(loc,2,"R"));
			loc = loc + 2;}
		}
		return loc;
	}
	
	public static void main(String args[])throws IOException{
		
		int loc = 0, cL=0, cL2=0;

		File file = new File(args[0]); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		String stt; 
	    while ((stt = br.readLine()) != null){
			
			System.out.print("\nLoc: "+loc+"\t");
			String arr[] = stt.split(" ");
			System.out.println(Arrays.toString(arr) + arr.length);

			if(arr.length >= 3){
				if(!Arrays.asList(psuedoOp).contains(arr[0]) && !Arrays.asList(twoByteOp).contains(arr[0]) && !Arrays.asList(fourByteOp).contains(arr[0])){
							
					//Symbol
					st.put(arr[0], new Details(loc,1,"R"));
					if(Arrays.asList(psuedoOp).contains(arr[1])){
						if(arr[1].equals("EQU")){
							if(arr[2].equals("*")){
								st.put(arr[0],new Details(loc,1,"R"));
							}else
							st.put(arr[0],new Details(Integer.parseInt(arr[2]),1,"A"));
						}							
						else if(arr[1].equals("DC")){
							st.put(arr[0], new Details(loc,4,"R"));
							dsdc.put(arr[0],arr[2].substring(2,arr[2].length()-1));
							loc+=4;						}
						else if(arr[1].equals("DS")){
							st.put(arr[0],new Details(loc,4,"R"));
							dsdc.put(arr[0],arr[2].substring(0,arr[2].length()-1));
							if(arr[2].contains("F")){
								loc += Integer.valueOf(arr[2].substring(0,arr[2].length()-1))*4;
							}
							else if(arr[2].contains("H")){
								loc += Integer.valueOf(arr[2].substring(0,arr[2].length()-1))*2;
							}
						}
					}
					else{

						im.put(loc, new Values(arr[1],arr[2]));

						if(arr[1].contains("=")){
							String literal = arr[1].substring(3);
							String tem[] = arr[1].split(",");
							literal=tem[1].substring(1);
							if(arr[1].contains("=F")){
								lt.put(literal,new Details(loc,4,"R"));
								cL++;
							}
							else if(arr[1].contains("=H")){
								lt.put(literal,new Details(loc,2,"R"));
								cL2++;
							}
							else if(arr[1].contains("=A")){
								lt.put(literal,new Details(loc,4,"R"));
								cL++;
							}	
						}
						if(Arrays.asList(twoByteOp).contains(arr[1])){
							st.put(arr[0], new Details(loc,2,"R"));
							loc +=2;
						}
						else if(Arrays.asList(fourByteOp).contains(arr[1])){
							st.put(arr[0], new Details(loc,4,"R"));
							loc +=4;
						}
					}
				}
			}
			else {
				if(arr.length > 1){
					if(Arrays.asList(psuedoOp).contains(arr[0])){
						if(arr[0].equals("USING")){
							im.put(loc-1, new Values(arr[0],arr[1]));

						}
					}
					im.put(loc, new Values(arr[0],arr[1]));
					if(arr[1].contains("=")){
						String literal = arr[1].substring(3);
						String tem[] = arr[1].split(",");
							literal=tem[1].substring(1);
						if(arr[1].contains("=F")){
							lt.put(literal,new Details(loc,4,"R"));
							cL++;
						}
						else if(arr[1].contains("=H")){
							lt.put(literal,new Details(loc,2,"R"));
							cL2++;
						}
						else if(arr[1].contains("=A")){
								lt.put(literal,new Details(loc,4,"R"));
								cL++;
							}	
					}

					
				}
				
				
				if(Arrays.asList(twoByteOp).contains(arr[0])){
					loc+=2;
				}
				else if(Arrays.asList(fourByteOp).contains(arr[0])){
					loc+=4;
				}
				else if(arr[0].equals("LTORG")){
					loc = loc + (8-loc%8);
					loc = pool(loc);
				}
				

			}
		}
		System.out.println("______________________________PASS 1______________________________");
		display();
		displayLT();
		displayIC(0);
		
		System.out.println("\n______________________________PASS 2______________________________");
		
		System.out.println();
		passTwo();
		System.out.println(loc);
		displayBaseTable();
		// displayIC(1);    
	}
}

class Details{
	int value,length;
	String addressing;
	Details(int value,int length,String addressing){
		this.value = value;
		this.length = length;
		this.addressing = addressing;
	}
}

class Values{
	String instruction,vals;
	Values(String instruction, String vals){
		this.instruction = instruction;
		this.vals = vals;
	}

}

