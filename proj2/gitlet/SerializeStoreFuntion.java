package gitlet;

import java.io.IOException;

public interface SerializeStoreFuntion{

    /** 不同类实现该接口 来实现序列化存储 */
    public String SerializeStore() throws IOException;
}
