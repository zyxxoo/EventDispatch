package demo.zy.com.demo.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by tubban on 2015/8/10.
 */
public abstract class AbsParams implements Serializable{
    public abstract Map<String, Object> toMap();
}
