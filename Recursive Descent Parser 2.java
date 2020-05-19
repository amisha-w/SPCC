import java.util.*;
import java.io.*;

class RDP2{
	static String w="";
	static int ptr=0;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		/*
			S->aPeQd
			P->bP/cP/Epsilon
			Q->ab/ac
		*/
		System.out.println("\tRECURSIVE DESCENT PARSER\n\nS->aPeQd\nP->bP/cP/Epsilon\nQ->ab/ac");
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
		// if(match('a')&&P()&&match('e')&&Q()&&match('d'))

		if(match('a'))
			if(P())
				if(match('e'))
					if(Q())
						if(match('d'))
							return true;
			
		return false;
	} 

	static boolean P(){
		// System.out.println(" entered P()");
		
		if(match('b')){
			if(P())
				return true;
			else
				ptr-=1;
		}
		else if(match('c')){
			if(P())
				return true;
			else
				ptr-=1;
		}
		return true;
	} 

	static boolean Q(){
		if(match('a')){
			if(match('b'))
				return true;
			else if(match('c'))
				return true;
			else
				ptr-=1;
		}
		else{
			if(match('d'))
				if(match('c'))
					return true;
				else
					ptr-=1;
		}

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

