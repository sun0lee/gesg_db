package com.gof.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;

public interface HisEntity<T> {

	public static final String getterPrefix = "get";

	public T convertToBiz(String baseYymm, int seq);

}


