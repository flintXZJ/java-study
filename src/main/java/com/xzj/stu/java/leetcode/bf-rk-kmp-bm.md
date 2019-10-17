### 字符串匹配 「BF 算法、RK 算法、BM 算法、KMP 算法、Sunday算法」

> [数据结构与算法之美笔记: 字符串匹配 「BF 算法、RK 算法、BM 算法、KMP 算法」](https://blog.csdn.net/zhanglong_4444/article/details/90671650)    
> [从头到尾彻底理解KMP](https://www.cnblogs.com/zhangtianq/p/5839909.html)  

#### BF
```
    public int bfAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        //haystack字符串当前匹配到的字符下标
        int i = 0;
        //needle字符串当前匹配到的字符下标
        int j = 0;
        while (i < haystack.length() && j < needle.length()) {
            if (haystack.charAt(i) == needle.charAt(j)) {
                i++;
                j++;
            } else {
                //初始haystack比较字符的下一位
                i = i - j + 1;
                //初始needle比较字符位置
                j = 0;
            }
        }

        //如果haystack中有needle字符，while循环肯定是j == needle.length()退出的
        if (j == needle.length()) {
            return i - j;
        }

        return -1;
    }
```


#### RK
```
public int rkAlgorithm(String haystack, String needle) {
    if (needle == null || needle.length() == 0) {
        return 0;
    }
    if (haystack == null || haystack.length() < needle.length()) {
        return -1;
    }

    int hlen = haystack.length();
    int nlen = needle.length();
    int nhash = needle.hashCode();

    int index = 0;
    while (index < hlen) {
        if (index + nlen - 1 < hlen) {
            String substring = haystack.substring(index, index + nlen);
            if (substring.hashCode() == nhash && substring.equals(needle)) {
                return index;
            }
            index++;
        } else {
            break;
        }
    }

    return -1;
}
```


#### KMP
> [从头到尾彻底理解KMP](https://www.cnblogs.com/zhangtianq/p/5839909.html) 

```
    public int kmpAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) return 0;
        if (haystack == null || haystack.length() < needle.length()) return -1;

        int i = 0;//haystack匹配下标
        int j = 0;//needle匹配下标
        int hlen = haystack.length();
        int nlen = needle.length();

        int[] next = getKMPNext(needle, nlen);
        while (i < hlen && j < nlen) {
            if (j == -1 || haystack.charAt(i) == needle.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }

        if (j == nlen) return i - j;
        return -1;
    }


    public int[] getKMPNext(String needle, int nlen) {
        int[] next = new int[nlen];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < nlen - 1) {
            if (k == -1 || needle.charAt(j) == needle.charAt(k)) {
                k++;
                j++;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

```

与BF算法相比：
kmp算法就是为了在比较中让模式串尽量右移，从而达到提高效率效果

KMP的算法流程：
假设现在文本串S匹配到 i 位置，模式串P匹配到 j 位置  
* 如果j = -1，或者当前字符匹配成功（即S[i] == P[j]），都令i++，j++，继续匹配下一个字符；  
* 如果j != -1，且当前字符匹配失败（即S[i] != P[j]），则令 i 不变，j = next[j]。
此举意味着失配时，模式串P相对于文本串S向右移动了j - next [j] 位。换言之，当匹配失败时，模式串向右移动的位数为：失配字符所在位置 - 失配字符对应的next 值，
即移动的实际位数为：j - next[j]，且此值大于等于1。(重点理解：i没变)

next 数组各值的含义：代表当前字符之前的子字符串中，有多大长度的相同前缀后缀。例如如果next [j] = k，代表j 之前的字符串中有最大长度为k 的相同前缀后缀。



#### BM

#### Sunday