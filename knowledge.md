#### 一.get和post的区别？

1.get再浏览器回退时是无害的，post会再次提交请求

2.get地址可以被保存为标签，post不能

3.get请求参数有长度限制，post没有

4.post比get更安全，因为get参数直接暴露在url上，所以不能用来传递敏感信息

5.get参数通过url传递，post放在request body中



#### 二、set和map的数据结构如何理解

1.Set是一种叫做集合的数据结构，Map是一个叫做字典的数据结构

共同点：集合、字典都可以存储不重复的值

不同点：集合是以[值，值]的形式存储元素，字典是以[键，值]的形式存储

```
let a = new Set([1, 2, 3]);
let b = new Set([4, 3, 2]);

// 并集
let union = new Set([...a, ...b]);
// Set {1, 2, 3, 4}

// 交集
let intersect = new Set([...a].filter(x => b.has(x)));
```



#### 三、字符串的常用方法有哪些？



```
let stringValue = "hello world";
console.log(stringValue.slice(3)); // "lo world"
console.log(stringValue.substring(3)); // "lo world"
console.log(stringValue.substr(3)); // "lo world"
console.log(stringValue.slice(3, 7)); // "lo w"
console.log(stringValue.substring(3,7)); // "lo w"
console.log(stringValue.substr(3, 7)); // "lo worl"
```



四、js本地存储的方法有哪些？

cookie , sessionStorage,localStorage,indexedDB

cookie

1.不超过4kb

2.过期时间之前一直有效，即使窗口或者浏览器关闭

localhostorage

1.生命周期：持久化的本地存储，除非主动删除，否则数据永远不会过期

2.大小：5M

3.存储内容多的话会消耗内存空间，会导致页面变卡

缺点：

1.无法设置过期时间

2.只能存入字符串，无法直接存对象



#### 四、let,const解决了什么问题

分别从var let const区别

1.var具有声明提升，let/const没有

2.var/let声明的变量可以重新赋值，const必须初始化而且不能重新赋值

3.var是es5引入的关键字，let,const是es6引入的关键字



#### 五、回流与重绘

1.引起DOM树结构变化，页面布局发生变化的行为就是回流

2.只是样式的变化，不会引起DOM树变化，页面布局变化的行为叫重绘

3.回流必定引起重绘，而重绘不一定引起回流

4.回流的代价要远远大于重绘

如何减少回流

1、减少对DOM的增删行为

​    比如删除某个节点，给某个父元素添加子元素

2、减少几何属性的变化

​    比如元素的宽高变化，boeder变化，字体大小变化

3.减少元素位置的变化

比如修改一个元素的margin,padding

4.减少获取元素的偏移量属性

例如获取一个元素的scrollTop,scrollLeft等属性，浏览器为了保证值的正确也会回流取得最新的值

5.浏览器窗口尺寸变化

resize事件也会引起回流



六、css隐藏元素

1、overflow:hidden

用来隐藏元素溢出部分，占据空间，无法响应事件

2、opacity:0

用来设置元素透明度，设置透明度为0相对于隐藏了元素，可以响应事件

3.display:none

彻底隐藏了元素，不占据空间，也不会影响布局

4.visibility:hidden

元素隐藏仍然占据空间，无法响应事件

5.position

通过position溢出可视区域

6.z-index

将元素置于其他元素下方



ul li:nth-child(-n+2)  //选择列表的前两个li



#### 六、empty()和has()隐藏空元素

1.查找子元素为空的父元素

.box:not(span:has("empty")){display:none}

2.查找不包含某个子元素的父元素

.box:not(.box:has(.text)){display:none}



#### 七、盒模型

1.标准盒模型  content-box

width=content-width

2.IE盒模型    border-box

width=content-width+padding-width+border-width



#### 八、v-if与v-show的区别

手段：v-if是动态的向DOM树内添加或者删除DOM元素，v-show是通过设置DOM元素的display样式属性控制显隐

编译过程：v-if切换有一个局部编译/卸载的过程，切换过程中合适地销毁和重建内部的事件监听和子组件，v-show只是简单的基于css切换

编译条件:v-if是惰性的，如果初始条件为假，则什么也不做，只有在条件第一次变为真时才开始局部编译，v-show是在任何条件下，无论首次是否为真，都被编译，然后被缓存，而且DOM元素保留

消耗性能：v-if有更高的切换消耗，v-show有更高的初始渲染消耗

使用场景：v-if适合运营条件不大可能改变，v-show适合频繁切换



data为什么是一个函数而不是对象

1.js中的对象是引用类型的数据，当多个实例引用同一个对象时，只要一个实例对这个对象进行操作，其他实例中的数据也会发生变化

2.而在vue中，更多的是想要复用组件，那就需要每个组件都有自己的数据，这样组件之间才不会相互干扰

3.所以组件的数据不能写成对象的形式，而是要写成函数的形式。数据以函数返回值的形式定义，这样当每次复用组件的时候，就会返回一个新的data,也就是说每个组件都有自己的私有数据空间，它们各自维护自己的数据，不会干扰其他组件的正常运行



#### 九、谈谈MVVM

1.MVVM分为Model、View、ViewModel

2.model代表数据模型，数据和业务逻辑都在Model层中定义

3.View代表UI视图，负责数据的展示

4.ViewModel负责监听Model中数据的改变并且控制视图的更新，处理用户交互操作

5.Model和View并无直接关联，而是通过ViewModel来进行联系的，Model和ViewModel之间有着双向数据绑定的联系。因此当Model中的数据改变时会触发View层的刷新，View中抑郁用户交互操作而改变的数据也会在Model中同步

6.这种模式实现了Model和View的数据自动同步，因此开发者只需要专注于数据的维护操作即可，而不需要自己操作DOM

#### 十、使用Object.defineProperty()来进行数据劫持有什么缺点

1、在对一些属性进行操作时，使用这种方法无法拦截，比如通过下标方式修改数组数据或者给对象新增属性，这都不能触发组件的重新渲染，因为Object.defineProperty不能拦截到这些操作。更精确的来说，对于数组而言，大部分操作都是拦截不到的，只是Vue内部通过重写函数的方式解决了这个问题

2.在Vue3.0中已经不使用这种方式了，而是通过使用proxy对对象进行代理，从而实现数据劫持。使用proxy的好处是它可以完美的监听到任何方式的数据改变，唯一的缺点是兼容性的问题，因为proxy是es6的语法



#### 十一、Vue子组件和父组件执行顺序

加载渲染过程

父组件beforeCreate

父组件created

父组件beforeMount

子组件beforeCreate

子组件created

子组件beforeMount

子组件mounted

父组件mounted

更新过程

父组件beforeUpdate

子组件beforeUpdate

子组件updated

父组件updated

销毁过程

父组件beforeDestory

子组件beforeDestory

子组件destroyed

父组件destroyed



#### 十二、对虚拟DOM的理解

1.从本质上来说，virtual dom是一个js对象，通过对象的方式来表示dom结构。将页面的状态抽象为js对象的形式，配合不同的渲染工具，使跨平台渲染成为可能。通过事务处理机制，将多次dom修改的结果一次性的更新到页面上，从而有效的减少页面渲染的次数，减少修改dom的重绘重排次数，提高渲染性能。

2.虚拟dom是对dom的抽象，这个对象是更加轻量级的对dom的描述。它设计的最初目的，就是更好的跨平台，比如nodejs就没有dom，如果想实现ssr，那么一个方式就是借助虚拟dom，因为虚拟dom本身就是js对象。在代码渲染到页面之前，vue会把代码转换为一个对象。以对象的形式来描述真实dom结构，最终渲染到页面。在每次数据发生变化之前，虚拟dom都会缓存一份，变化之时，现在的虚拟dom会与缓存的虚拟dom进行比较。在vue内部封装了diff算法，通过这个算法来进行比较，渲染时修改改变的变化，原先没有发生改变的通过原先的数据进行渲染

3.另外现代前端框架的一个基本要求就是无须手动操作dom，一方面是因为手动操作dom无法保证程序性能，多人协作的项目中如果review不严格，可能会有开发者写出性能较低的代码，另一方面更重要的是省略手动dm操作可以大大提高开发效率



#### 十三、为什么不建议用index作为key

使用index作为key和没写基本上没区别，因为不管数组的顺序怎么颠倒，index都是0,1,2这样排列，导致vue会复用错误的旧子节点，做很多额外的工作



