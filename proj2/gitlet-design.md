# Gitlet Design Document

**Name**:Yang Dong

## Classes and Data Structures


### FileStructure
- .gitlet
   - blobs
   - pointers
   - commits


        

### Class:Commit 
#### Instance variable
* private String message - 提交时候的信息
* private Date commitDate - 提交日期
* private String parent - 父引用的名字
* private String Commit_FOLDER - Commit 序列化存储的文件夹

#### Instance Method

#### Static Method
* public static void CommitWrite() - 将Commit序列化写入文件夹


### Class:InitMethod
* private String master - 当前分支指针


### Class:Pointer 
* private String CurrentLocation - 存储当前指针所指向commit的哈希值



## Algorithms

## Persistence

