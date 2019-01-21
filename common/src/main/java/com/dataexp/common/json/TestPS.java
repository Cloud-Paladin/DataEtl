package com.dataexp.common.json;

public abstract class  TestPS {

    public static final int MAXNUM = 1;

   public int getMs() {
           return MAXNUM;
   }

    public int getMa() {
        return getMs();
    }

    public static void main(String[] args) {
        System.out.println(TestSon.MAXNUM);
        System.out.println(new TestSon().getMa());
        System.out.println(new TestSon().getMs());
        TestPS ps = (TestPS)(new TestSon());
        System.out.println(ps.getMa());
    }
}

class TestSon extends TestPS {
    public static final int MAXNUM = 2;

//    @Override
//    public int getMs() {
//        return maxnum;
//    }
}


