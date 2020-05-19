import java.util.*;
import java.io.*;

class RDP{
	static String w="";
	static int ptr=0;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		/*
			S->aPQ
			P->ab/c
			Q->b
		*/
		System.out.println("\tRECURSIVE DESCENT PARSER\n\nS->aPQ\nP->ab/c\nQ->b");
		int opt;
		System.out.print("\n-1.Exit\nEnter string: ");
		w = sc.next();
		while(!w.equals("-1")){
			ptr=0;
			if(S())
				System.out.println("\nString accepted: "+w);
			else
				System.out.println("\nString NOT accepted. substring matched: "+w.substring(0,ptr));
			// System.out.println("ptr: "+ptr);
			System.out.print("\n-1.Exit\nEnter string: ");
			w = sc.next();
		}
	}

	static boolean S(){
		if(match('a'))
			if(P())
				if(Q())
					return true;
			
		return false;
	} 

	static boolean P(){
		// System.out.println(" entered P()");
		

		if(match('a'))
		{
			if(match('b'))
				return true;
			else
				ptr-=1;
		}
			
		if(match('c'))
			return true;
		
		return false;
	} 

	static boolean Q(){
		if(match('b'))
			return true;
		return false;
	} 

	static boolean match(char c){
		if(ptr<w.length()&&w.charAt(ptr)==c){
			// System.out.print("-"+c);
			ptr++;
			return true;
		}
		return false;
	} 
}

