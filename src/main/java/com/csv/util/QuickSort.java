package com.csv.util;

import java.util.Arrays;

public class QuickSort {
    public static void quickSort(int[] arr, int startIndex, int endIndex) {
        // 递归结束条件：startIndex大等于endIndex的时候
        if (startIndex >= endIndex) {
            return;
        }
        // 得到基准元素位置
        int pivotIndex = partition(arr, startIndex, endIndex);
        // 用分治法递归数列的两部分
        quickSort(arr, startIndex, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, endIndex);
    }

    private static int partition(int[] arr, int startIndex, int endIndex) {
        // 取第一个位置的元素作为基准元素

        int pivot = arr[startIndex];
        int left = startIndex;
        int right = endIndex;
        // 坑的位置，初始等于pivot的位置
        int index = startIndex;

        System.out.println("This time start with pivot value: " + pivot + " left : " + left + " right : " + right);
        //大循环在左右指针重合或者交错时结束
        while (right >= left) {
            //right指针从右向左进行比较
            while (right >= left) {

                if (arr[right] < pivot) {
                    System.out.println("    From Right, When Value mix, arr[right] " + arr[right] + " , arr[left] " + arr[left] + " pivot : " + pivot);
                    arr[left] = arr[right];
                    index = right;
                    System.out.println("         From Right, new index : " + index);
                    left++;
                    System.out.println("        ++ current pivot : " + pivot );
                    break;
                }
                System.out.println("        ++ current pivot : " + pivot );
                right--;
            }
            //left指针从左向右进行比较
            while (right >= left) {
                if (arr[left] > pivot) {
                    System.out.println("    From Left, When Value mix, arr[right] " + arr[right] + " , arr[left] " + arr[left] + " pivot : " + pivot);

                    arr[right] = arr[left];
                    index = left;
                    System.out.println("         From Left, new index : " + index);
                    right--;
                    System.out.println("        ++ current pivot : " + pivot );
                    break;
                }
                System.out.println("        ++ current pivot : " + pivot );
                left++;
            }
        }
        arr[index] = pivot;
        System.out.println(Arrays.toString(arr));
        return index;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{15, 17, 4, 7, 11, 6, 10, 12, 5, 14, 3, 2, 13, 8, 16, 1};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));

    }
}
