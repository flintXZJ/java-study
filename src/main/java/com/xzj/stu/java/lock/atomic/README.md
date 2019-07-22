## AtomicInteger
#### 常见AtomicLong、AtomicBoolean，他们的实现原理相同，区别在与运算对象类型的不同。令人兴奋地，还可以通过AtomicReference<V>将一个对象的所有操作转化成原子操作。
#### java.util.concurrent.atomic
#### 通常AtomicInteger的性能是ReentantLock的好几倍。

### 实现原理