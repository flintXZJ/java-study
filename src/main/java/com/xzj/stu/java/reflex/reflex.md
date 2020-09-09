## java反射原理(reflex)
> jdk 1.8.0_221  
> [大家都说 Java 反射效率低，你知道原因在哪里么](https://juejin.im/post/5da33b2351882509334fc0d3)
> [关于反射调用方法的一个log](https://www.iteye.com/blog/rednaxelafx-548536)

### 1、获取反射方法

#### 1.1、getMethod
包括所有的public方法，包括父类的方法

```
@CallerSensitive
public Method getMethod(String name, Class<?>... parameterTypes)
    throws NoSuchMethodException, SecurityException {
    //检查方法权限
    checkMemberAccess(Member.PUBLIC, Reflection.getCallerClass(), true);
    //获取方法 Method 对象
    Method method = getMethod0(name, parameterTypes, true);
    if (method == null) {
        throw new NoSuchMethodException(getName() + "." + name + argumentTypesToString(parameterTypes));
    }
    return method;
}
```

Member.PUBLIC的定义，规范了可以获取的对象
```
public interface Member {
    /**
     * Identifies the set of all public members of a class or interface,
     * including inherited members.
     */
    public static final int PUBLIC = 0;

    /**
     * Identifies the set of declared members of a class or interface.
     * Inherited members are not included.
     */
    public static final int DECLARED = 1;
}
```

重点是关注源码中如何获取Method
```  
private Method getMethod0(String name, Class<?>[] parameterTypes, boolean includeStaticMethods) {
    MethodArray interfaceCandidates = new MethodArray(2);
    Method res =  privateGetMethodRecursive(name, parameterTypes, includeStaticMethods, interfaceCandidates);
    if (res != null)
        return res;

    // Not found on class or superclass directly
    interfaceCandidates.removeLessSpecifics();
    return interfaceCandidates.getFirst(); // may be null
}
```
通过privateGetMethodRecursive()获取方法，如果返回方法为空，获取方法数组中的第一个不为NULL的方法。
继续追踪privateGetMethodRecursive
```  
private Method privateGetMethodRecursive(String name,
        Class<?>[] parameterTypes,
        boolean includeStaticMethods,
        MethodArray allInterfaceCandidates) {
    Method res;
    // Search declared public methods 
    //1、privateGetDeclaredMethods(true) 获取本类的public方法，并筛选符合条件的方法
    if ((res = searchMethods(privateGetDeclaredMethods(/* publicOnly */ true),
                             name,
                             parameterTypes)) != null) {
        if (includeStaticMethods || !Modifier.isStatic(res.getModifiers()))
            return res;
    }
    //2、Search superclass's methods 获取父类的方法 调用getMethod0递归调用privateGetMethodRecursive()方法
    if (!isInterface()) {
        Class<? super T> c = getSuperclass();
        if (c != null) {
            if ((res = c.getMethod0(name, parameterTypes, true)) != null) {
                return res;
            }
        }
    }
    //3、Search superinterfaces' methods 获取接口中对应的方法
    Class<?>[] interfaces = getInterfaces();
    for (Class<?> c : interfaces)
        if ((res = c.getMethod0(name, parameterTypes, false)) != null)
            allInterfaceCandidates.add(res);
    // Not found
    return null;
}
```
继续追踪privateGetDeclaredMethods()方法
```  
private Method[] privateGetDeclaredMethods(boolean publicOnly) {
    checkInitted();
    Method[] res;
    //1、通过缓存获取 Method[]
    ReflectionData<T> rd = reflectionData();
    if (rd != null) {
        res = publicOnly ? rd.declaredPublicMethods : rd.declaredMethods;
        if (res != null) return res;
    }
    // No cached value available; request value from VM
    //2、没有缓存，通过 JVM 获取
    res = Reflection.filterMethods(this, getDeclaredMethods0(publicOnly));
    if (rd != null) {
        if (publicOnly) {
            rd.declaredPublicMethods = res;
        } else {
            rd.declaredMethods = res;
        }
    }
    return res;
}
```

追踪reflectionData()，发现会在class中维护一个ReflectionData的软引用，作为类信息的数据缓存
```
// Lazily create and cache ReflectionData
private ReflectionData<T> reflectionData() {
    SoftReference<ReflectionData<T>> reflectionData = this.reflectionData;
    int classRedefinedCount = this.classRedefinedCount;
    ReflectionData<T> rd;
    if (useCaches &&
        reflectionData != null &&
        (rd = reflectionData.get()) != null &&
        rd.redefinedCount == classRedefinedCount) {
        return rd;
    }
    // else no SoftReference or cleared SoftReference or stale ReflectionData
    // -> create and replace new instance
    return newReflectionData(reflectionData, classRedefinedCount);
}
```
ReflectionData 结构如下：
``` 
private static class ReflectionData<T> {
    volatile Field[] declaredFields;
    volatile Field[] publicFields;
    volatile Method[] declaredMethods;
    volatile Method[] publicMethods;
    volatile Constructor<T>[] declaredConstructors;
    volatile Constructor<T>[] publicConstructors;
    // Intermediate results for getFields and getMethods
    volatile Field[] declaredPublicFields;
    volatile Method[] declaredPublicMethods;
    volatile Class<?>[] interfaces;

    // Value of classRedefinedCount when we created this ReflectionData instance
    final int redefinedCount;

    ReflectionData(int redefinedCount) {
        this.redefinedCount = redefinedCount;
    }
}
```
可以看到，保存了Class中的属性和方法。 如果缓存为空，就会通过getDeclaredMethods0从JVM中查找方法。getDeclaredMethods0是一个native方法。

通过上面的分析可以得出获取method的整体步骤：  
class.getMethod() --> getMethod0() --> privateGetMethodRecursive() --> privateGetDeclaredMethods(true) --> reflectionData()

#### 1.2、getDeclaredMethod
获取所有本类定义的方法，public、protected、private都在此，但是不包括父类的方法。

通过分析源码获取getDeclaredMethod整体步骤：
class.getDeclaredMethod() --> privateGetDeclaredMethods(false) --> reflectionData()

通过对比可以发现getDeclaredMethod比getMethod方法少了privateGetMethodRecursive()递归获取父类method的过程


### 2、调用反射方法
获取方法method之后，通过Method.invoke()调用方法
```
@CallerSensitive
public Object invoke(Object obj, Object... args)
    throws IllegalAccessException, IllegalArgumentException,
       InvocationTargetException
{
    if (!override) {
        //1、检查权限
        if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
            Class<?> caller = Reflection.getCallerClass();
            checkAccess(caller, clazz, obj, modifiers);
        }
    }
    //2、获取MethodAccessor
    MethodAccessor ma = methodAccessor;             // read volatile
    if (ma == null) {
        ma = acquireMethodAccessor();
    }
    //3、调用 MethodAccessor.invoke
    return ma.invoke(obj, args);
}
```
可以看出Method.invoke方法的实现分三步：
1、判断override变量，如果override == true，则跳过检查。可以通过在调用Method.invoke()之前，调用Method.setAccessible(true)设置。
