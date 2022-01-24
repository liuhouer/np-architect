package com.imooc.jvm.jvm;

class EscapeTest1 {
    public static SomeClass someClass;

    // 全局变量赋值逃逸
    public void globalVariablePointerEscape() {
        someClass = new SomeClass();
    }

    // 方法返回值逃逸
    // someMethod(){
    //   SomeClass someClass = methodPointerEscape();
    // }
    public SomeClass methodPointerEscape() {
        return new SomeClass();
    }

    // 实例引用传递逃逸
    public void instancePassPointerEscape() {
        this.methodPointerEscape()
            .printClassName(this);
    }

    public void someTest() {
        SomeTest someTest = new SomeTest();
        someTest.age = 1;
        someTest.id = 1;

        // 开启标量替换之后，
        int age = 1;
        int id = 1;
    }
}

class SomeClass {
    public void printClassName(EscapeTest1 escapeTest1) {
        System.out.println(escapeTest1.getClass().getName());
    }
}

class SomeTest {
    int id;
    int age;
}