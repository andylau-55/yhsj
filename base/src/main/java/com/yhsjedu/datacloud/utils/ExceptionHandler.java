package com.yhsjedu.datacloud.utils;

/**
 * 异常处理类,以运行时异常的形式将指定的异常抛出
 *
 */
public class ExceptionHandler {

    /**
     * 抛出指定类型的异常信息<br/>
     * 注意: 这里的返回值只是为了绕过编译器的异常检查,不会真正返回
     *
     * @param e
     * @return
     */
    public static RuntimeException throwException(Throwable e) {
        if (e == null) {
            NullPointerException ex = new NullPointerException();
            int size = ex.getStackTrace().length - 1;
            StackTraceElement[] st = new StackTraceElement[size];
            System.arraycopy(ex.getStackTrace(), 1, st, 0, size);
            ex.setStackTrace(st);
            throw ex;
        }
        ExceptionHandler.<RuntimeException>throwException0(e);
        return null;
    }


    /**
     * 检查obj对象是否为空,如果为空,则抛出异常
     *
     * @param obj  被检查的对象
     * @param info 抛出异常的信息
     */
    public static void tryNullException(Object obj, String info) {
        tryNullException(obj, new NullPointerException(info));
    }

    /**
     * 检查obj对象是否为空,如果为空,则抛出异常
     *
     * @param obj   被检查的对象
     * @param cause 抛出的异常
     */
    public static void tryNullException(Object obj, Exception cause) {
        if (obj == null) {
            throwException(cause);
        }
    }

    /**
     * 根据表达式判断是否抛出异常,如果表达式为真,则抛出异常
     *
     * @param expression 布尔表达式
     * @param info       抛出异常的信息
     */
    public static void tryThrowException(boolean expression, String info) {
        tryThrowException(expression, new RuntimeException(info));
    }

    /**
     * 根据表达式判断是否抛出异常,如果表达式为真,则抛出异常
     *
     * @param expression 布尔表达式
     * @param cause      抛出的异常
     */
    public static void tryThrowException(boolean expression, Exception cause) {
        if (expression) {
            throwException(cause);
        }
    }


    /**
     * 将异常转换为特定类型异常抛出
     *
     * @param e 异常
     */
    @SuppressWarnings("unchecked")
	private static <T extends Throwable> void throwException0(Throwable e) throws T {
        throw (T) e;
    }

}
