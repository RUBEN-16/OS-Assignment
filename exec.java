public class exec {
    public static void main(String[] args) {
        int[] number = {2,1,4,5,0,3};
        int iteration = 5;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < iteration; j++){
                if(number[j] > number[j+1]){
                    int tempNUmber = number[j];
                    number[j] = number[j + 1];
                    number[j + 1] = tempNUmber;
                }
            }
            iteration--;
        }
        for(int prs: number){
            System.out.printf(" %d", prs);
        }
    }
}
