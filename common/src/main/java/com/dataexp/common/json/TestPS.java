package com.dataexp.common.json;

public abstract class  TestPS {

    public static final int maxnum = 1;

   public int getMs() {
           return maxnum;
   }

    public int getMa() {
        return getMs();
    }

    public static void main(String[] args) {
        System.out.println(new TestSon().maxnum);
        System.out.println(new TestSon().getMa());
        System.out.println(new TestSon().getMs());
        TestPS ps = (TestPS)(new TestSon());
        System.out.println(ps.getMa());
    }
}

class TestSon extends TestPS {
    public static final int maxnum = 2;

//    @Override
//    public int getMs() {
//        return maxnum;
//    }
}


