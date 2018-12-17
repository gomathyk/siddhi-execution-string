/*
 * Copyright (c)  2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.extension.siddhi.execution.string;

import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.ReturnAttribute;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.exception.SiddhiAppValidationException;

import java.util.Map;

/**
 * coalesce(arg1,arg2,...,argN)
 * returns the value of the first of its input parameters that is not NULL
 * Accept Type(s): Arguments can be of any type, given that the argument count is more than zero and all the
 * arguments are of the same type.
 * Return Type(s): Same type as the input
 */

@Extension(
        name = "coalesce",
        namespace = "str",
        description = " This returns the first input parameter value of the given argument, that is not null.",
        parameters = {
                @Parameter(name = "argn",
                        description = "It can have one or more input parameters in any data type." +
                                " However, all the specified " +
                                "parameters are required to be of the same type.",
                        type = {DataType.INT, DataType.LONG, DataType.DOUBLE, DataType.FLOAT,
                                DataType.STRING, DataType.BOOL, DataType.OBJECT})
        },
        returnAttributes = @ReturnAttribute(
                description = "This holds the first input parameter that is not null.",
                type = {DataType.INT, DataType.LONG, DataType.DOUBLE, DataType.FLOAT,
                        DataType.STRING, DataType.BOOL, DataType.OBJECT}),
        examples = @Example(
                syntax = "coalesce(null, \"BBB\", \"CCC\")",
                description = "This returns the first input parameter that is not null. " +
                "In this example, it returns \"BBB\".")
)
public class CoalesceFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType;

    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {
        int attributeCount = 0;
        if (attributeExpressionExecutors.length == 0) {
            throw new SiddhiAppValidationException("str:coalesce() function requires at least one argument, " +
                    "but found only " + attributeExpressionExecutors.length);
        }
        Attribute.Type type = attributeExpressionExecutors[0].getReturnType();
        for (ExpressionExecutor expressionExecutor : attributeExpressionExecutors) {
            attributeCount++;
            if (type != expressionExecutor.getReturnType()) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the " + attributeCount +
                        "'th argument of str:coalesce() function, required " + type + ", but found "
                        + attributeExpressionExecutors[attributeCount - 1].getReturnType().toString());
            }
        }
        returnType = type;
    }

    @Override
    protected Object execute(Object[] data) {
        for (Object aData : data) {
            if (aData != null) {
                return aData;
            }
        }
        return null;
    }

    @Override
    protected Object execute(Object data) {
        return data;
    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

    @Override
    public Map<String, Object> currentState() {
        return null;    //No need to maintain a state.
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }
}
