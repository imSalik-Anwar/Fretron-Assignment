import java.util.Scanner;

public class Main{
    public static boolean getRahimShare(int[] apples, boolean[] visited, int rahimApplesWeight, String rahimApples, int idx, int n, String[] answer, int[] shares){
        /**
         * The logic is the same as the previous two functions getRamShare() and getShamShare().
         * The only difference is that here we are finding the final combination of apples so we don't need to calls
         * any further function from the base case.
         * 
         * if we have found a combination in this function, that means we have found a valid answer.
         * We revert back to the previuous functions.
         **/
        if(rahimApplesWeight == 0){
            answer[2] = rahimApples;
            return true;
        }
        if(rahimApplesWeight < 0 || idx == n){
            return false;
        }
        
        boolean take = false;
        if(apples[idx] <= rahimApplesWeight && !visited[idx]){
            visited[idx] = true;
            take = getRahimShare(apples, visited, rahimApplesWeight - apples[idx], rahimApples+" "+apples[idx], idx+1, n, answer, shares);
            if (take) return true; // Stop recursion if found a valid distribution
            visited[idx] = false;
        }
        boolean noTake = getRahimShare(apples, visited, rahimApplesWeight, rahimApples, idx+1, n, answer, shares);
        
        return take || noTake;
            
    }
    public static boolean getShamShare(int[] apples, boolean[] visited, int shamApplesWeight, String shamApples, int idx, int n, String[] answer, int[] shares){
        /**
         * The logic is the same as the previous function getRamShare(). 
         * Only difference being, we are fincding a combination of apples to accomodate Shame's share-weight
         * so we pass values specific to Sham and store the asnwerin the answer array accordingly.
         **/
        if(shamApplesWeight == 0){
            answer[1] = shamApples;
            boolean isPossible = getRahimShare(apples, visited, shares[2], "", 0, n, answer, shares);
            if(isPossible) return true;
            return false;
        }
        if(shamApplesWeight < 0 || idx == n){
            return false;
        }
        
        boolean take = false;
        if(apples[idx] <= shamApplesWeight && !visited[idx]){
            visited[idx] = true;
            take = getShamShare(apples, visited, shamApplesWeight - apples[idx], shamApples+" "+apples[idx], idx+1, n, answer, shares);
            if (take) return true; // Stop recursion if found a valid distribution
            visited[idx] = false;
        }
        boolean noTake = getShamShare(apples, visited, shamApplesWeight, shamApples, idx+1, n, answer, shares);
        
        return take || noTake;
            
    }
    public static boolean getRamShare(int[] apples, boolean[] visited, int ramApplesWeight, String ramApples, int idx, int n, String[] answer, int[] shares){
        /**
         * Base case: 
         * if Ram's has got apples such that he has got his share, we save his apples and go for finding Sham's apples.
         * */
        if(ramApplesWeight == 0){
            // Save Ram's apples.
            answer[0] = ramApples;
            // Find Sham's apples
            boolean isPossible = getShamShare(apples, visited, shares[1], "", 0, n, answer, shares);
            // If everything is fine, return true (success) else false (failure).
            if(isPossible) return true;
            return false;
        }
        // If a combination is not possible or we have serached through all the apples without finding a solution, return false.
        if(ramApplesWeight < 0 || idx == n){
            return false;
        }
        
        /**
         * We pick (take) an apples only when it's not exceeding Ram's share after being added to Ram's share
         * and that apple has not been taken already.
         * */
        boolean take = false;
        if(apples[idx] <= ramApplesWeight && !visited[idx]){
            // If the apples is taken, mark it true and move onto the next apple.
            visited[idx] = true;
            take = getRamShare(apples, visited, ramApplesWeight - apples[idx], ramApples+" "+apples[idx], idx+1, n, answer, shares);
            if (take) return true; // Stop recursion if found a valid distribution
            visited[idx] = false;
        }
        // Move onto the next apple in case current apple is not taken.
        boolean noTake = getRamShare(apples, visited, ramApplesWeight, ramApples, idx+1, n, answer, shares);
        // Return true if a combination was found otherwise return false.
        return take || noTake;
            
    }
	public static void main(String[] args) {
	    // Taking input using Scanner class.
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] apples = new int[n];
		int totalAppleWeight = 0;
		for(int i = 0; i < n; i++){
		    apples[i] = sc.nextInt();
		    // Calculate the toal weight of all the apples simultaniously.
		    totalAppleWeight += apples[i];
		}
		// Solution ==================================================================================================
		
		// get everyone's share
		int ramShare = totalAppleWeight * 50/100;
		int shamShare = totalAppleWeight * 30/100;
		int rahimShare = totalAppleWeight * 20/100;
		// Store everyone's share into an array to make them kind of global so that they can be used in function calls.
		int[] shares = new int[]{ramShare, shamShare, rahimShare};
		
		// Keep an boolean visted array to keep track of picked apples.
		boolean[] visited = new boolean[n];
		// Keep an String array to store answers.
		String[] answer = new String[3];
		/**
		 * Approach:
		 * Call a recursive function to find the combination of apples such that their weight equals to Ram's share.
		 * If we found a combination for Ram's share, we go for finding Sham's combination and then for Rahim's.
		 * If at any point we fail to find a desired combination, we turn back to the previous fucntion to re-find
		 * a new combination.
		 * 
		 * If we find the desired output, we print it corresponding to their respective holder.
		*/
		
		// Calling a function for getting Ram's share of apples
		boolean isPossible = getRamShare(apples, visited, shares[0], "", 0, n, answer, shares);
		
		// Printing the output
		if(isPossible){
		    System.out.println("Ram: "+answer[0]);
		    System.out.println("Sham: "+answer[1]);
		    System.out.println("Rahim: "+answer[2]);
		} else {
		    System.out.println("Fare disdribution is not possible.");
		}
		sc.close();
	}
}
