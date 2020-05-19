import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Err{
	int line,word;
	String s;
	Err(String ss,int l,int w){
		line=l;
		s=ss;
		word=w;
	}
	public String toString(){
		return "Error term: "+this.s+" Error Line: "+this.line+" Error Word: "+this.word+"";
	}

}

class Li{
	String lit;
	int type;/* 1-int 2-float 3-string*/
	Li(String l,int t){
		type=t;
		lit=l;
	}
	public String toString(){
		String tt;
		if(type==1){
			tt="Integer";
		}
		else if(type==2){
			tt="Float";
		}
		else{
			tt="String";
		}
		return "literal: "+this.lit+"\ttype:"+tt+"";
	}
}

class Lex{
	static String[] keywords = new String[]{"include","int","float","char","double","bool","void","extern","unsigned","goto","static","class","struct","for","if","else","return","register","long","while","do"};
	static String[] func = new String[]{"main", "printf","scanf"};
	static String[] header = new String[]{"conio.h","stdio.h","iostream.h"};
	static String[] operators = new String[]{"+","-","=","*","/","%","!","&","|",">","<"};
	static String[] specsym = new String[]{"{","}","(",")","#",";","[","]",","};
	static Vector<String> userfunc = new Vector<String>();
	static Vector<String> symbols = new Vector<String>();
	static Vector<Li> literals = new Vector<Li>();
	static Vector<Err> errors = new Vector<Err>();

	static int check(String[] array, String item){
		return Arrays.asList(array).indexOf(item);
	}

	static Li Number(String word){
		for(Li l:literals){
			if(l.lit.equals(word)){
				return l;
			}
		}
		if(!Character.isDigit(word.charAt(0)) || word.matches(".*[a-z].*")||word.matches(".*[A-Z].*")){
			return null;
		}
		if (word.contains(".")&& word.indexOf(".")!=word.charAt(0) &&word.lastIndexOf(".")!=word.length()-1 && word.indexOf(".")==word.lastIndexOf(".")) { //float
			Li l = new Li(word,2);
			literals.add(l);
			return l;
		}
		Li l = new Li(word,1);
		literals.add(l);
		return l;
	}

	static int Symbol(String word,String s){
		Pattern p = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$", Pattern.CASE_INSENSITIVE);//,[a-zA-Z0- 9]$
		Matcher m = p.matcher(word);
		if(m.find()==false){
			return -1;
		}
		if(word.length()>31){
			return -1;
		}
		else if(!Character.isLetter(word.charAt(0))&& word.charAt(0)!='_'){
			return -1;
		}

			if(s.equals("-1"))
			return 1;
		else if(s.equals("("))
			return 2;
		return -1;
	}

	static int stringLiteral(String word){

		if((word.charAt(0)=='\''&&word.charAt(word.length()-1)=='\'')||(word.charAt(0)=='\"'&&word.charAt(word.length()-1)=='\"')){
			return 1;
		}
		return -1;
	}

	static int getWord(String keyword, int w, String charAti, int line) throws Error{
		int y = check(keywords,keyword);
								int y1 = check(func,keyword);
								int y2 = check(header,keyword);
								Li y3 = Number(keyword);
								int y4 = Symbol(keyword,charAti);
								int y5 = stringLiteral(keyword);

								w+=1;

								if(y!=-1)  
									System.out.print("\n\tKeyword #"+y+": "+keyword);
								else if(y1!=-1)
									System.out.print("\n\tFunction #"+y1+": "+keyword);
								else if(y2!=-1)
									System.out.print("\n\tHeader #"+y2+": "+keyword);
								else if(y3!=null){
									int i1= literals.indexOf(y3);
									if(y3.type==1){
										System.out.print("\n\tLiteral(Integer)#"+i1+": "+keyword);
									}
									else{
										System.out.print("\n\tLiteral(Float)#"+i1+": "+keyword);
									}
									
								}
								else if(y4!=-1){
									if(y4==1){
										if(!symbols.contains(keyword)){
											symbols.add(keyword);
										}
										System.out.print("\n\tSymbol#"+symbols.indexOf(keyword)+": "+keyword);
										
									}
									else if(y4==2){
										if(!userfunc.contains(keyword)){
											userfunc.add(keyword);
										}
										System.out.print("\n\tUserFunction#"+userfunc.indexOf(keyword)+": "+keyword);
									}
								}
								else if(y5!=-1){
									
									int i1=0;
									Li l=new Li(keyword,3);
									if(!literals.contains(l)){
										literals.add(l);
									}
									i1=literals.indexOf(l);
									System.out.print("\n\tLiteral(String)#"+i1+": "+keyword);
								}
								else{
									 throw new Error(new Err(keyword,line,w));
								}
								return w;
	}

	public static void main(String[] args) {
		try{
		File file = new File("test.cpp"); 
  
	  	BufferedReader br = new BufferedReader(new FileReader(file)); 
	  	int line =0;
		String st; 
		while ((st = br.readLine()) != null){
			System.out.println();
			line+=1;
			int w=0;
			String[] lex ;
			lex = st.split(" ");
			System.out.println("\nLine"+line+": "+st);
			for(String strr: lex){
				int begin=0,end=0,flag=0;
				for(int i=0;i<strr.length();i++){
					if( !Character.isDigit(strr.charAt(i)) && !Character.isLetter(strr.charAt(i)) &&strr.charAt(i)!='\''&&strr.charAt(i)!='\"'&&strr.charAt(i)!='.'){
						int in=check(operators,Character.toString(strr.charAt(i)));
						int in1=check(specsym,Character.toString(strr.charAt(i)));
						

						if(in!=-1){
							flag=1;
							String keyword = strr.substring(begin,end);
							if(begin!=end)
							{
								try{
									w = getWord(keyword,w,"-1",line);
								}
								catch(Error e){
									System.out.println("\n----------------------"+e+"\n----------------------");
									errors.add(e.e);
								}

						    }
							begin=end=i+1;
							
							System.out.print("\n\tOperator #"+in+": "+strr.charAt(i));
						}
						else if(in1!=-1){
							flag=1;
							String keyword = strr.substring(begin,end);
							if(begin!=end)
							{
								try{
								w = getWord(keyword,w,String.valueOf(strr.charAt(i)),line);
							}
								catch(Error e){
									System.out.println("\n----------------------"+e+"\n----------------------");
									errors.add(e.e);

								}
						    }
							begin=end=i+1;
							System.out.print("\n\tSpecialSym #"+in1+": "+strr.charAt(i));
						}

					}
					else{
						end+=1;
					}
					if(i==strr.length()-1&&flag==0){
						String keyword=strr;
							try{
								w = getWord(keyword,w,"-1",line);}
								catch(Error e){
									System.out.println("\n----------------------"+e+"\n----------------------");
									errors.add(e.e);

								}
						 
					}
				}
			}


		}
		}catch(Exception e){
			System.out.println(e);
		}
		System.out.println("\n\nSymbol Table: ");
		for(int k=0;k<symbols.size();k++){
			System.out.println("#"+k+"\t"+symbols.elementAt(k));
		}
			System.out.println("\nLiterals Table: ");
			for(int k=0;k<literals.size();k++){
			System.out.println("#"+k+"\t"+literals.elementAt(k));
		}
			System.out.println("\nUserFunction Table: ");
			for(int k=0;k<userfunc.size();k++){
			System.out.println("#"+k+"\t"+userfunc.elementAt(k));
		}
			System.out.println("\nError Table: ");
			for(int k=0;k<errors.size();k++){
			System.out.println("#"+k+"\t"+errors.elementAt(k));
		}
	}
}

class Error extends Exception{
   Err e;
   Error(Err er) {
   	e=er;
   }
   public String toString(){
     return ("\n\nERROR\nError Term: "+e.s+" Error Line: "+e.line+" Error Word: "+e.word);
  }
}