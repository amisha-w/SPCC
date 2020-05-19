import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class OPP{
	static char[] stack = new char[100];
	static Vector<Character> chars = new Vector();
	static int top=0;
	public static void main(String[] args) throws FileNotFoundException, IOException{
		File file = new File(args[0]); 
		Scanner sc = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String stt;
		int n=0;
		System.out.println("\nInput Grammar:");
	    while ((stt = br.readLine()) != null){
	    	System.out.println(stt);
	    	String lr[] = stt.split("->");
	      	String te[] = lr[1].split("/");
	      	for(String i:te){
	      		for(int j=0;j<i.length();j++){
	      			if(!Character.isUpperCase(i.charAt(j))&&chars.indexOf(i.charAt(j))==-1){
	      				chars.add(i.charAt(j));
	      				n++;
	      			}
	      		}
	      	}
	    }
	    chars.add('$');
      	n++;
      	System.out.println("\nTotal "+n+" chars: "+chars.toString());
      	int a[][] = makePrecedenceTable(n);
      	System.out.print("\nOperator Precedence Table:\n \t");
      	for(char c : chars)
      		System.out.print(c+"\t");
      	System.out.println();
      	for(int i=0;i<n;i++){
      		for(int j=0;j<n;j++){
				if(j==0){
					System.out.print(chars.get(i)+"\t");
				}
				if(a[i][j]==-1)
				System.out.print("-\t");
				else if(a[i][j]==0)
				System.out.print("<\t");
				else if(a[i][j]==1)
				System.out.print(">\t");
			}
			System.out.println();
		}
		int opt;
		System.out.print("\n-1.Exit\nEnter string: ");
		String w = sc.next();
		while(!w.equals("-1")){
			w+="$";
			stack = new char[100];
			top=0;
			push('$');
			if(isParsable(w,a))
				System.out.print("\nString "+w+" is parsable by Operator Precedence Parser");
			else
				System.out.print("\nString "+w+" is NOT parsable by Operator Precedence Parser");

			System.out.print("\n\n-1.Exit\nEnter string: ");
			w = sc.next();
		}
	}

	public static boolean isSpecial(char c){
		if (String.valueOf(c).matches("[^a-zA-Z0-9]")) {
    		return true;
		}
		return false;
	}

	public static boolean isParsable(String s, int[][] a){
		int i=0;
		while(i<s.length()){

			if(chars.indexOf(s.charAt(i))==-1){
				return false;
			}
			
			System.out.print("String symbol: "+s.charAt(i)+" ");
			printStack();
			int v = a[chars.indexOf(stack[top])][chars.indexOf(s.charAt(i))];
			if(v==0){
				push(s.charAt(i));
				i++;
			}
			else if(v==1){
				char c = pop();
			}
			else if(v==-1){
				if(s.charAt(i)=='$')
					return true;
				else 
					return false;
			}
		}
		return false;
	}

	public static int[][] makePrecedenceTable(int n){
		int a[][] = new int[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(chars.get(i)==chars.get(j)){
					if(chars.get(i)=='i'||chars.get(i)=='$'){
						a[i][j] = -1; //'-'
					}else{
						a[i][j] = 1; //>
					}
				}else{
					if(precedence(chars.get(i))>precedence(chars.get(j))){
						a[i][j] = 1; //>
					}else{
						a[i][j] = 0; //<
					}
				}
			}
		}
		return a;
	}

	public static int precedence(char a){
		switch(a){
			case '$': return 0;
			case '+': return 1;
			case '-': return 1;
			case '*': return 2;
			case 'i': return 3;
			default: return -1;
		}
	}

	public static void push(char a){
		top++;
		stack[top] = a;
	}

	public static void printStack(){
		System.out.print("Stack: ");
		for(int i=0;i<=top;i++){
			System.out.print(stack[i]+" ");
		}
		System.out.println();

	}

	public static char pop(){
		char a = stack[top];
		top--;
		return a;
	}

	
}