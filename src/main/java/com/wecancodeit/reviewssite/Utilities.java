package com.wecancodeit.reviewssite;

public class Utilities
{
  public static Tag[] concatenateTagArrays(Tag[] array1, Tag[] array2)
  {
    Tag[] answer = new Tag[array1.length + array2.length];
    int i;
    for (i=0; i < array1.length; i++){
      answer[i] = array1[i];
    }
    for(i=0; i < array2.length; i++){
      answer[i + array1.length] = array2[i];
    }
    return answer;
  }

  public static String formatTag(String rawString){
    return rawString.trim().toUpperCase();
  }
}