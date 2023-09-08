package com.humingamelab.mc.api;

import com.humingamelab.mc.Client.type.LogType;

import java.util.HashMap;


public class ApiTest {
    public static void main(String[] args) throws Exception {
        Api api = new Api();

//        HashMap map = new HashMap();
//        map.put("x", 829);
//        map.put("y", 322);
//        LogInput input = LogInput.builder()
//                .message("Test from java")
//                .playerName("drewharris1")
//                .type(LogType.PlaceBlock)
//                .attributes(map)
//                .build();
//        SubmitLogMutation m = SubmitLogMutation.builder().input(input).build();
//        SubmitLogMutation.Data add = api.executeMutation(m).get();
//
//        // Get all logs
//        GetAllLogsQuery.Data logs = api.executeQuery(GetAllLogsQuery.builder().build()).get();
//        System.out.println(logs.logs.get(0).attributes);

        // with attributes
        api.log(LogType.PlaceBlock, "drewharris1", "Test from java",
                new HashMap<String, Object>() {{
                    put("x", "829");
                    put("y", "322");
                }}
        );

        // without attributes
        api.log(LogType.MFPGraphReview, "drewharris1", "Reviewed graph", null);
    }
}
