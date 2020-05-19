import java.util.*;
import java.io.*;

class Main{
  static Vector<String> mdt = new Vector();
  static Vector<String> im = new Vector();
  static LinkedHashMap<Integer,Integer> ala_index = new LinkedHashMap<>();
  // static LinkedHashMap<String,String> def = new LinkedHashMap<>();
  static Vector<MNT> mnt = new Vector();
  static LinkedHashMap<Integer,String> ala = new LinkedHashMap<>();
  static int mdtc=1,mntc=1,mdtp=0;
  static int alac=0,ff=0;
  public static void main(String[] args) throws IOException{

    //PASS1
    File file = new File(args[0]); 
    BufferedReader br = new BufferedReader(new FileReader(file)); 
    int flag=0,f=0; //flag=macro starts; f=macro name line read
    String stt; 
      while ((stt = br.readLine()) != null){
      
      String arr[] = stt.split(" ");
      // System.out.println(arr[0]);
      if(arr[0].equals("MACRO")){
        flag=1;
        f=1;
        continue;
      }
      else if(arr[0].equals("MEND")){
        mdt.add(arr[0]);
        mdtc++;
        flag=0;
        continue;
      }
      else{
        
        String name="";
        String temp[]=null;
        if(flag==1){
          if(f==1){//macro name line
            if(arr[0].contains("&")){ //label is non-null
              ala_index.put(mntc,ala.size());
              ala.put(alac++,arr[0]);
              name+=arr[0]+" "+arr[1]+" ";
              mnt.add(new MNT(mntc++,arr[1],mdtc));
              
              temp=arr[2].split(",");
            }else{
              ala_index.put(mntc,ala.size());
              ala.put(alac++,"null");
              
              name += arr[0];
              mnt.add(new MNT(mntc++,name,mdtc));

              if(arr.length>1){
                temp=arr[1].split(",");
                ala_index.put(mntc-1,ala.size());
              }
              name+=" ";
            }
            if(temp!=null){
                for(int i=0;i<temp.length;i++){
                  // System.out.println("arg"+i+": "+temp[i]);
                  if(temp[i].contains("=")){
                    int h = temp[i].indexOf("=");
                    ala.put(alac++,temp[i].substring(0,h));
                    name+=temp[i].substring(0,h)+"="+temp[i].substring(h+1);
                    if(i!=temp.length-1)
                      name+=",";
                  }
                  else{
                      ala.put(alac++,temp[i]);
                      name+=temp[i];
                      if(i!=temp.length-1)
                        name+=",";
                    }
                }
              }
            mdt.add(name);
            mdtc++;
            f=0;
            continue;
          }
          else{//body lines
            int ala_start=-1;
            try{
              ala_start= ala_index.get(mntc-1);
            }
            catch(Exception e){}
            String strr="";
            if(arr[0].contains("&")){
                String arg=arr[0];
                int in=0;
                for(Map.Entry<Integer,String> entry: ala.entrySet()){
                  if(in<ala_start){
                    in++;
                    continue;
                  }
                  Integer key = entry.getKey();
                  String value = entry.getValue();
                  if(value.equals(arg)){
                    arg="#"+key;
                    break;
                  }
                }
                if(arg.contains("&")){
                  System.out.println("Invalid argument "+arg+". Argument not found in definition line");
                  System.exit(0);
                }
                strr+=arg+" ";
                for(int i=1;i<arr.length-1;i++){
                  strr+=arr[i]+" ";
                }
            }
            else{
              for(int i=0;i<arr.length-1;i++){
              strr+=arr[i]+" ";
            }
            }
            
            
            String temp1[]=arr[arr.length-1].split(",");
            for(int i=0;i<temp1.length;i++){
              String arg="";
              if(temp1[i].contains("+")){
                int in=0;
                arg = temp1[i].substring(0,temp1[i].indexOf("+"));
                for(Map.Entry<Integer,String> entry: ala.entrySet()){
                  if(in<ala_start){
                    in++;
                    continue;
                  }
                  Integer key = entry.getKey();
                  String value = entry.getValue();
                  if(value.equals(arg)){
                    arg="#"+key;
                    break;
                  }
                }
                if(arg.contains("&")){
                  System.out.println("Invalid argument "+arg+". Argument not found in definition line");
                  System.exit(0);
                }
                strr+=arg+temp1[i].substring(temp1[i].indexOf("+"));
              }
              else if(temp1[i].contains("-")){
                int in=0;
                arg = temp1[i].substring(0,temp1[i].indexOf("-"));
                for(Map.Entry<Integer,String> entry: ala.entrySet()){
                  if(in<ala_start){
                    in++;
                    continue;
                  }
                  Integer key = entry.getKey();
                  String value = entry.getValue();
                  if(value.equals(arg)){
                    arg="#"+key;
                    break;
                  }
                }
                if(arg.contains("&")){
                  System.out.println("Invalid argument "+arg+". Argument not found in definition line");
                  System.exit(0);
                }
                strr+=arg+temp1[i].substring(temp1[i].indexOf("-"));
              }   
              else{
                arg=temp1[i];
                int in=0;
                for(Map.Entry<Integer,String> entry: ala.entrySet()){
                  if(in<ala_start){
                    in++;
                    continue;
                  }
                  Integer key = entry.getKey();
                  String value = entry.getValue();
                  if(value.equals(arg)){
                    arg="#"+key;
                    break;
                  }
                }
                if(arg.contains("&")){
                  System.out.println("Invalid argument "+arg+". Argument not found in definition line");
                  System.exit(0);
                }
                strr+=arg;
              }
              if(i!=temp1.length-1)
                strr+=",";    
            }
            mdt.add(strr);
            mdtc++;
          }
        }
        else{
          im.add(stt);
        }
      }

    }//while ends
    System.out.println("\n__________________________PASS 1__________________________\n");
    displayMDT();
    displayMNT();
    displayALA();
    System.out.println("\nIntermediate Code:");
    for(String s: im){
      System.out.println(s);
    }

    //PASS2
    System.out.println("\n__________________________PASS 2__________________________\n");
    int label=0,ala_start=-1;
    for(String s:im){
      String arr[]=s.split(" ");
      mdtp=-1;
      for(MNT n: mnt){
        if(n.name.equals(arr[0]))
        {
          try{
            ala_start = ala_index.get(n.index);}catch(Exception e){}
          mdtp=n.mdt_index;
          break;
        }
        else if(arr.length>1&&n.name.equals(arr[1])){
          label=1;
          try{
          ala_start = ala_index.get(n.index);}catch(Exception e){}
          mdtp=n.mdt_index;
          break;
        }
      }
      if(mdtp==-1){
        System.out.println(s);
      }
      else{//macro called
        //all args def; 2 args def; keywords
        String arguments[]=null;
        String arguments2[]=null;
        if(label==0&&arr.length>=2)//incr argss
          arguments= arr[1].split(",");
        else if(label==1&&arr.length==3)//loop incr argss
          arguments= arr[2].split(",");
        else if(label==0&&arr.length==1)//incr
          arguments= null;
        else if(label==1&&arr.length==2)//loop incr
          arguments= null;
        String name = mdt.get(mdtp-1);
        // System.out.println("-----NAME LINE: "+name);
        String temp[] = name.split(" ");
        String strr="";
        for(int i=0;i<temp.length-1;i++){
          strr+=temp[i]+" ";
        }
        //definition args
        int def=0;
        if(temp.length>=2){//macro has arguments
          arguments2=temp[temp.length-1].split(",");
        
          for(int z=0;z<arguments2.length;z++){
            if(arguments2[z].contains("="))
              def++;
          } 
        }
        int len1,len2;
        try{
          len1 = arguments.length;
        }catch(Exception e){
          len1=-1;
        }
        try{
          len2 = arguments2.length;
        }
        catch(Exception e){
          len2=-1;
        }
        if(len1!=-1&&len2!=-1&&Math.abs(len2-len1)>def){
          System.out.println("\nERROR\n\tNot enough arguments in Macro call for \""+name+"\"");
          System.exit(0);
        }else if(len1!=-1&&len2!=-1&&len1>len2||(len2==-1&&len1!=-1)){
          System.out.println("\nERROR\n\tToo many arguments in Macro call for \""+name+"\"");
          System.exit(0);
        }
        if(len2!=-1&&len1!=-1){//no empty 
          int in=0;

            for(Map.Entry<Integer,String> entry: ala.entrySet()){
                  if(in<ala_start){
                    in++;
                    continue;
                  }
                  if(len2==0)
                    break;
                  int fl=0;
                  Integer key = entry.getKey();
                  String value = entry.getValue();
                  if(len2==arguments2.length&&label==1){//label assign 
                    value=arr[0];
                    ala.put(key,value);
                    label=0;
                    continue;
                  }
                  
                  for(String y: arguments){//assign acc to key
                    if(fl==0&&y.contains(value)){
                      value=y.split("=")[1];
                      ala.put(key,value);
                      fl=1;
                      break;
                    }
                  }
                  if(fl==1){
                    len2--;
                      continue;
                  }
                  for(int z=0;z<arguments.length;z++){ // assign acc to position
                    if(!arguments[z].contains("=")){
                      value=arguments[z];
                      arguments[z]+="=";
                      ala.put(key,value);
                      fl=1;
                      break;
                    }
                  }
                  if(fl==1){
                    len2--;
                      continue;
                  }
                  for(String y: arguments2){//check default value(if there)
                    if(y.contains(value+"=")){
                      value=y.split("=")[1];
                      ala.put(key,value);
                      fl=1;
                      break;
                    }                                
                  }
                  if(fl==1){
                    len2--;
                      continue;
                  }
                  System.out.println("\nERROR\n\tNot enough arguments.");
            System.exit(0);
          }        
       }
       else if(len2!=-1){//no args in call but there in definition with def
            int in=0;
            for(Map.Entry<Integer,String> entry: ala.entrySet()){
                  if(in<ala_start){
                    in++;
                    continue;
                  }
                  if(len2==0)
                    break;
                  int fl=0;
                  Integer key = entry.getKey();
                  String value = entry.getValue();
                  for(String y: arguments2){//check default value
                    if(y.contains(value+"=")){
                      value=y.split("=")[1];
                      ala.put(key,value);
                      fl=1;
                      break;
                    }                                
                  }
                  if(fl==1){
                    len2--;
                    continue;
                  }

System.out.println("\nERROR\n\tNot enough arguments.");
            System.exit(0);
            }

       }

       //here
       // System.out.println(mdtp);
       while(!mdt.get(mdtp-1).equals("MEND")){
        String str="";
        mdtp++;
        String ss1 = mdt.get(mdtp-1);
        if(ss1.equals("MEND")){
          break;
        }
        String hh[] = ss1.split(" ");
        for (int i=0; i<hh.length; i++){
          if(!hh[i].contains("#")){
            str+=hh[i]+" ";
          }else{
            String em[] = hh[i].split(",");
            for(int j=0; j<em.length; j++){
              String sss = em[j];
              if(!sss.contains("#"))
              if(j==em.length-1)
                str+=sss+" ";
                else
                str+=sss+",";
              else{
                String arg="";
                if(!sss.contains("+")&&!sss.contains("-"))
                  arg = ala.get(Integer.parseInt(String.valueOf(sss.substring(1))));
                else
                  {
                    if(sss.contains("+")){
                      arg = ala.get(Integer.parseInt(String.valueOf(sss.substring(1,sss.indexOf("+")))));
                      arg+=sss.substring(sss.indexOf("+"));
                    }
                    else{
                      arg = ala.get(Integer.parseInt(String.valueOf(sss.substring(1,sss.indexOf("-")))));
                      arg+=sss.substring(sss.indexOf("-"));
                    }
                  }
                 if(j==em.length-1)
                str+=arg+" ";
                else
                str+=arg+",";
              }
            }
          }

        }
        //======prev=======
        // char[] ss = ss1.toCharArray(); 
        // for (int i=1; i<=ss.length; i++){ 
        //     if(ss[i-1]=='#') { 
              
        //         String arg = ala.get(Integer.parseInt(String.valueOf(ss[i])));  
        //         str+=arg; 
        //         i++; 
        //     } 
        //     else
        //       str+=ss[i-1];
        // }
        System.out.println(str); 
       }//while ends

      }
    }
    displayALA();
  }

  static void displayALA(){
    System.out.println("\n\t---ARGUMENT LIST ARRAY---");
    System.out.println("\nIndex Argument");
    for(Map.Entry<Integer,String> entry: ala.entrySet()){
      Integer key = entry.getKey();
      String value = entry.getValue();
      System.out.println(key+"\t"+value);
    }
  }

  // static void displaydef(){
  //  System.out.println("\n\t---DEFAULT---");
  //  System.out.println("\nIndex Argument");
  //  for(Map.Entry<String,String> entry: def.entrySet()){
  //    String key = entry.getKey();
  //    String value = entry.getValue();
  //    System.out.println(key+"\t"+value);
  //  }
  // }

  static void displayMNT(){
    int i=0;
    System.out.println("\n\t---MACRO NAME TABLE---");
    System.out.println("\nIndex Macro_Name MDT_Index");
    for(MNT n: mnt){
      System.out.println(n.index+"\t"+n.name+"\t"+n.mdt_index);
    }
  }

  static void displayMDT(){
    int i=0;
    System.out.println("\n\t---MACRO DEFINITION TABLE---");
    System.out.println("\nIndex\tDefinition");
    for(String s : mdt){
      i++;
      System.out.println(i+"\t"+s);
    }
  }

}

class MNT{
  int index;
  String name;
  int mdt_index;
  MNT(int index,String name, int mdt_index){
    this.index=index;
    this.name=name;
    this.mdt_index=mdt_index;
  }

}