> 参考：https://blog.csdn.net/apple_wolf/article/details/81331475


### 继承
接口是常量值和方法定义的集合。接口是一种特殊的抽象类。
 
java类是单继承的。classB Extends classA
java接口可以多继承。Interface3 Extends Interface0, Interface1, interface……

不允许类多重继承的主要原因是，如果A同时继承B和C，而b和c同时有一个D方法，A如何决定该继承那一个呢？  
但接口不存在这样的问题，接口全都是抽象方法继承谁都无所谓，所以接口可以继承多个接口。
 

注意：   
1）一个类如果实现了一个接口，则要实现该接口的所有方法。  
2）方法的名字、返回类型、参数必须与接口中完全一致。如果方法的返回类型不是void，则方法体必须至少有一条return语句。  
3）因为接口的方法默认是public类型的，所以在实现的时候一定要用public来修饰（否则默认为protected类型，缩小了方法的使用范围）。




### HashMap
resize()  扩容逻辑
> https://www.toutiao.com/a6726809084521087496/
```
if (oldTab != null) {
    for (int j = 0; j < oldCap; ++j) {
        Node<K,V> e;
        if ((e = oldTab[j]) != null) {
            oldTab[j] = null;
            if (e.next == null)
                newTab[e.hash & (newCap - 1)] = e;
            else if (e instanceof TreeNode)
                ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
            else { // preserve order
                Node<K,V> loHead = null, loTail = null;
                Node<K,V> hiHead = null, hiTail = null;
                Node<K,V> next;
                do {
                    next = e.next;
                    if ((e.hash & oldCap) == 0) {
                        if (loTail == null)
                            loHead = e;
                        else
                            loTail.next = e;
                        loTail = e;
                    } else {
                        if (hiTail == null)
                            hiHead = e;
                        else
                            hiTail.next = e;
                        hiTail = e;
                    }
                } while ((e = next) != null);
                if (loTail != null) {
                    loTail.next = null;
                    newTab[j] = loHead;
                }
                if (hiTail != null) {
                    hiTail.next = null;
                    newTab[j + oldCap] = hiHead;
                }
            }
        }
    }
}
```
![](http://p3.pstatp.com/large/pgc-image/1540890421927f9679a3a3d)


#### 代码块的执行顺序
静态代码块>构造代码块>构造函数>普通代码块
