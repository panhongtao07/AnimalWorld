## 内容

在动物世界中，有各种各样的动物。
这一天，它们准备进行一次吃西瓜大赛，来证明各自的食力。
计划仓促的缘故，西瓜的个数十分有限，幸好旅游的一组师生经过，把西瓜切成了数块。
比赛有最长时间，参与者可以提前退出，最终成绩将由食量和速度共同决定。
选手准备的都不太充分？最终的冠军，又会是谁呢？

## 文件结构

##### 项目结构

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

#### 动物世界

- `animal`: 动物所在的文件夹，包括各类动物（人也是动物）
    - `RandomBehavedAnimal`: 本次模拟的动物均是随机行动的，实际的动物均继承自此类
- `food`: 食物所在的文件夹，这次比赛里只有西瓜
    - `Melon`: 西瓜是可切分的食物，被切过的西瓜会呈现为“西瓜块”，和完整的西瓜相区分
    - `MelonComparator`: 比较器，按序摆放西瓜的依据
- `util`: 工具类所在的文件夹
    - `Display`: 方便呈现的工具函数，主要是居中对齐、控制中文长度和浮点位数
    - `Out`: 输出流，用于屏蔽或调整输出
- `AnimalWorld`: 主文件，运行动物世界并开始比赛

## 运行

Run `java --enable-preview -cp bin AnimalWorld` in this directory.

## 个性化设置

#### 个性化比赛

调整`src/AnimalWorld.java`文件以设置个性化动物世界

- 全局设置: `AnimalWorld`类的静态常量
- 准备部分: `gatherAnimals`, `prepareMelons`会准备参赛者和西瓜，寻找并切西瓜
    - 全局设置`cutCount`和`oriCount`控制西瓜的个数
- 比赛部分: `startEating`开始比赛
    - 全局设置`maxRound`和`gameTime`控制默认的比赛轮数和时长限制
    - 全局设置`waitFullAnimals`和`enableFullAnimalActions`控制是否考虑已吃饱的动物
- 总结部分:
    - `ranking`: 排名
    - `printFoods`和`printAnimals`: 打印最终的环境状态
    - `printGrade`和`printWinner`: 公布成绩和获奖者，前者打印一个表格

#### 自定义世界

- 加入喜欢的动物和食物
    - 在food文件夹加入更多种类的食物
        - 不同的度量单位，通过`getQuantityString`来个性化呈现，但请保证quantity的存储单位相同
        - 通过`toRawString`来呈现不同的食物，最终会呈现为"食物[剩余量/已吃完]"的形式
    - 在animal文件夹加入更多种类的动物
        - 呈现动物名称: `toString`
        - 决定动物的食物偏好: `acceptFood`
        - 决定动物行为：如何选择食物、如何进食、吃多少、各种情况下如何决策
        - 自定义专属行为
        - 动物的通用行为
            - 占有食物，声明所有权: `chooseFoods`(`addFood(s)`)
                - 只能占有自己愿意吃的，但可以同时占有多个食物
            - 吃占有的食物、或者抢吃任何食物: `eat`
                - 默认情况下一次只吃一个食物
            - 吃完或者放弃所有权: `finishEating`
            - 吃饱后，不再接受新食物，不再吃东西，但可以不立刻放弃所有权
        - 使用默认的`RandomBehavedAnimal`简化随机行动
            - `behaveRandomly`定义决策
            - 从已有的食物中随机挑选单个食物
            - 随机进食量和时间
- 在`src/AnimalWorld.java`文件调整比赛过程
    - 加入更多种类的食物，并定义对他们的操作！（可能也需要切开？）
    - `startEating`只适合那些**随机行动**的动物
    - `finishEating`在总结时假设所有食物都是西瓜，这不会出错，但可能令人啼笑皆非。你可能需要修改措辞！
