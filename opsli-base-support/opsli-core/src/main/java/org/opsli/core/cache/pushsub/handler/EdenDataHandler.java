/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.CacheConstants;
import org.opsli.common.constants.DictConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.handler
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:24
 * @Description: 永久数据处理
 */
@Slf4j
public class EdenDataHandler implements RedisPushSubHandler{

    @Autowired
    EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.EDEN_DATA;
    }

    @Override
    public void handler(JSONObject msgJson) {
        String key = (String) msgJson.get(MsgArgsType.CACHE_DATA_KEY.toString());
        Object value = msgJson.get(MsgArgsType.CACHE_DATA_VALUE.toString());
        CacheType type = CacheType.valueOf((String )msgJson.get(MsgArgsType.CACHE_DATA_TYPE.toString()));

        // 解析 key
        String handleKey = CacheUtil.handleKey(CacheConstants.EDEN_DATA,key);

        // 缓存更新
        if(CacheType.UPDATE == type){
            ehCachePlugin.put(CacheConstants.HOT_DATA, handleKey, value);
        }
        // 缓存删除
        else if(CacheType.DELETE == type){
            ehCachePlugin.delete(CacheConstants.HOT_DATA, handleKey);
        }

    }

}
