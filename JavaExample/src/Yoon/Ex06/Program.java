package Yoon.Ex06;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;

public class Program {
	
	private int choice; // 메뉴선택
	private BankVO bank;
	private List<CustomerVO> customerList;
	private ScannerManage scan;
	private boolean result;
	
	public Program() {
		this.bank = new BankVO();
		new CustomerVO();
		this.scan = new ScannerManage();
		this.customerList = new ArrayList<CustomerVO>();
	}
	
	public List<CustomerVO> getCustomerList() {
		return customerList;
	}
	
	public void setCustomerList(List<CustomerVO> customerList) {
		this.customerList = customerList;
	}
	
	
	public int getChoice() {
		return choice;
	}

	public void setChoice() { // choice 에 대한 InputMismatchException예외처리
		while(true){
			try{
				this.choice = scan.inputScannerInt();
				break;
			}
			catch(InputMismatchException ime){
				System.out.println("1,2,3,4,5 중에 입력해주세요.");
			}
		}
	}

	public void startProgram() {
		int choice = 0;
		bank.setTotalMoney(1000000000); // 초기 은행 돈 10억으로 설정
		while ( true ) {
			System.out.println("========bank 국민은행 대출========");
			System.out.println("1.대출");
			System.out.println("2.돈갚기");
			System.out.println("3.대출내역보기");
			System.out.println("4.파산신청");
			System.out.println("5.종료");
			System.out.println("==============================="); // 메뉴
			
			this.setChoice();
			choice = this.getChoice(); // choice 입력받고 예외처리
			
			if ( choice == 1 ) {
				while(true){
					System.out.println("이름, 전화번호, 빌리고 싶은 돈(원), 담보를 입력하세요.(최대 대출 가능액: 1억)");
					String name = scan.inputScanner();
					if(scan.checkScanner("^[가-힣a-zA-Z]*$", name)){ //이름에 대한 체크
						System.out.println("재입력");
					}
					else{
						if ( this.checkName(name) ){
							String phoneNumber = scan.inputScanner();
							int money = scan.inputScannerInt();// 예외처리해야해
							this.checkInputMoney(name, phoneNumber, money);
							bank.printBankInfo(); // 대출 후 은행 돈 출력
							break;
						}
						else {
							System.out.println("새로운 정보로 가입해주세요.");
						}
					}//else
				}//while
			}//if
			else if ( choice == 2 ) { // 돈갚기
				while (true) {
					System.out.println("이름을 입력해주세요.");
					String searchName = scan.inputScanner();
					if(scan.checkScanner("^[가-힣a-zA-Z]*$", searchName)){ //searchName  체크
					}
					else{
						if ( this.returnMoney(searchName) ){
							break;
						}
						else {
							System.out.println(searchName + "의 고객 정보가 없습니다.");
							break;
						}//else					
					}//else
				}//while
			}//else if
			else if ( choice == 3 ) { // 대출내역 확인
				while(true){
					System.out.println("이름을 입력해주세요.");
					String searchName = scan.inputScanner();
					if ( scan.checkScanner("^[가-힣a-zA-Z]*$", searchName) ){// searchName 체크
					}
					else {
						this.searchMyInfo(searchName);
						bank.printBankInfo(); // 은행정보 출력
						break;
					}
				}
			}
			else if ( choice == 4 ) { // 파산신청
				while (true) {
					System.out.println("이름을 입력하세요.");
					String deleteName = scan.inputScanner();
					if ( scan.checkScanner("^[가-힣a-zA-Z]*$", deleteName) ){ // deleteName 체크
					}
					else{ 
						this.bankruptcy(deleteName);
						bank.printBankInfo();//은행정보 출력
						break;
					}//if
				}//while
			}//else if
			else if ( choice == 5){//프로그램 종료
				System.exit(0);
			}
		}//while
	}
	
	public boolean isResult() {
		return result;
	}

	public boolean setResult(boolean result) {
		this.result = result;
		return result;
	}
	
	/**
	 * inputMoney를 체크하는 메소드
	 * 프로세스
	 * 1) 담보 체크 (이미 구현 된 사항)
	 * 2) 1억 이상 대출 불가 
	 * 3) 은행에 있는 돈보다 크면 대출 불가
	 * 4) 대출이 가능한 상태라면 가입 진행
	 * 5) 해당 금액에 대해서 대출
	 * 6) 은행에 있는 돈이 0 이면 시스템 종료
	 * 
	 * @param name
	 * @param phoneNumber
	 * @param money
	 */
	public void checkInputMoney( String name, String phoneNumber, int money ) { // 
		String dambo = scan.inputScanner();
		if(scan.checkScanner("^[가-힣a-zA-Z]*$", dambo)){ // 담보에 대한 체크
			System.out.println("재입력");
		}
		else if (money > bank.MAX_LOAN){ // 1억 이상 빌리면 재입력
			System.out.println("1억 까지만 빌릴 수 있습니다.");
		}
		else if(money > bank.getTotalMoney()) { // 은행에 있는 돈보다 대출하려는 돈이 크면
			System.out.println("은행에 돈이 모자릅니다.");
		}
		else {
			Date curDate = new Date();
			Calendar now = Calendar.getInstance();
			long dateTime = now.getTimeInMillis();
			CustomerVO customer = new CustomerVO();
			
			customer.setName(name);
			customer.setPhoneNumber(phoneNumber);
			//customer.setMoney(money);
			customer.setDambo(dambo);
			customer.setLoanTime(curDate);
			customer.setMillisTime(dateTime);
			customer.setLoanMoney(money);
			
			this.customerList.add(customer);
			System.out.println("빌린 시간 : " + customer.getLoanTime()); // 가입 (대출 진행)
			
			int out = bank.getTotalMoney() - money; // 은행돈에서 빌린돈 빼
			bank.setTotalMoney(out); // 그돈을 은행 총금액에 넣어
			System.out.println(money + "원의 돈을 빌렸습니다.");
			
			if ( bank.getTotalMoney() == 0 ) {
				System.exit(0); // 은행돈이 0원이면 종료
			}
		}
	}
	
	/**
	 * 이름을 받아 파산하는 메소드
	 * 프로세스
	 * 1) customerList에 원하는 고객 정보가 있는지 조회
	 * 2) 있다면 nameCheck = true, 해당 고객 정보를 임시 저장
	 * 3) 없다면 nameCheck = false
	 * 3) 조건 문에 따라서 고객의 담보 정보를 출력하고 customerList에서 삭제
	 * @param deleteName
	 */
	public void bankruptcy(String deleteName) { // 파산하는 메소드
		
		boolean nameCheck = false;
		CustomerVO checkedCustomer = new CustomerVO();
		
		for ( CustomerVO customer : this.customerList ) {
			if ( customer.getName().equals(deleteName) ){
				checkedCustomer = customer;
				nameCheck =  true;
			}
		}
		nameCheck = false;
		
		if ( this.setResult(nameCheck) ){ //존재하면
				System.out.println("당신의 담보 " + checkedCustomer.getDambo() + "는 내가 가져간다." );// 담보 뺏기
				this.customerList.remove(checkedCustomer); // 기록 삭제
				System.out.println("정보삭제는 해줄게");
		}
		else{ //이름이 존재하지않으면
			System.out.println(deleteName+"의 정보가 없습니다.\n");
		}//else
	}
	
	/**
	 * 이름을 입력하여 대출 내역을 확인
	 * 프로세스
	 * 1) customerList에 원하는 고객 정보가 있는지 조회
	 * 2) 있다면 nameCheck = true, 해당 고객 정보를 임시 저장
	 * 3) 없다면 nameCheck = false
	 * 3) 조건 문에 따라서 고객 정보 출력 혹은 고객 정보 없음 메세지 출력
	 * @param searchName
	 */
	public void searchMyInfo(String searchName) { // 내가 빌린 돈의 정보를 찾는다.
		
		boolean nameCheck = false;
		CustomerVO selectedCustomer = new CustomerVO();
		
		for ( CustomerVO customer : this.customerList ) {
			if ( customer.getName().equals(searchName) ){
				selectedCustomer = customer;
				nameCheck =  true;
			}
		}
		nameCheck = false;
		
		if ( this.setResult(nameCheck)) { 
			this.printInfo(selectedCustomer);//해당하는 정보 출력
			System.out.println("");
		}
		else { 
			System.out.println(searchName + "의 고객 정보가 없습니다.");
		}
	}
	
	/**
	 * 빌린 돈을 갚는 메소드
	 * 프로세스
	 * 1) 갚고자 하는 사람의 정보 확인
	 * 2) 갚아야할 돈 계산 ( 전체 대출 금액 중 갚은 금액 제외)
	 * 3) 갚을 금액이 0이 될 때 까지 반복하여 갚을 돈을 입력한다.
	 * @param searchName
	 * @return
	 */
	public boolean returnMoney( String searchName ) { // 돈 갚을 때 
		boolean nameCheck = false;
		CustomerVO selectedCustomer = new CustomerVO();
		System.out.println("얼마를 갚을 건가요?");
		int money = scan.inputScannerInt();
		
		for ( CustomerVO customer : this.customerList ) {
			if ( customer.getName().equals(searchName) ){
				selectedCustomer = customer;
				nameCheck =  true;
			}
		}
		nameCheck = false;
		
		if (this.setResult(nameCheck)) {
			this.printInfo(this.getInfo(searchName)); // 찾은정보 출력
			
			Calendar when = Calendar.getInstance();
			long time = when.getTimeInMillis();
			long result = ((time - selectedCustomer.getMillisTime()) / 1000);
			int sc = (int)Math.round((double)result);
			double down = Math.floor(sc/5); // 소수점 내림
			double pow = Math.pow((1+bank.RATE),down);
			int totalLoan = (int)(selectedCustomer.getLoanMoney() * pow);
			
			int loan = totalLoan	-  this.getInfo(searchName).getRepayMoney(); // 갚아야할 돈 계산
			System.out.println("");
			bank.printBankInfo();// 은행 정보 출력
			if (money > loan) { // 갚아야할 돈보다 많으면
				System.out.println("그렇게 많이 안줘도 되는데...;;");
			} else if (money == loan) {
				System.out.println("돈을 전부 갚으셨습니다.");
				
				selectedCustomer.setRepayMoney(money); // 고객이 갚은 돈 쌓기
				bank.setTotalMoney(bank.getTotalMoney() + money); // 은행에 갚은 돈 만큼 총금액에 추가
				System.out.println(money + "만큼의 돈을 갚았습니다.");
				
				this.getCustomerList().remove(this.getInfo(searchName)); // 다갚았으니
																				// 정보삭제
				System.out.println("정보가 삭제되었습니다.\n");
			} else {
				selectedCustomer.setRepayMoney(money); // 고객이 갚은 돈 쌓기
				bank.setTotalMoney(bank.getTotalMoney() + money); // 은행에 갚은 돈 만큼 총금액에 추가
				System.out.println(money + "만큼의 돈을 갚았습니다.");
				bank.printBankInfo();
				System.out.println("");
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 이미 존재하는 고객인지 체크
	 * @param searchName 입력받은 이름
	 * @return
	 */
	public boolean checkName ( String searchName ){ 
		boolean nameCheck = false;
		
		for ( CustomerVO customer : this.customerList ) {
			if ( customer.getName().equals(searchName) ){
				nameCheck =  true;
			}
		}
		nameCheck = false;
		
		if ( this.setResult(nameCheck)) { 
			System.out.println("이미 존재하는 고객입니다.");
			return false;
		}
		else { 
			return true;
		}
		
	}

	/**
	 * 정보 출력
	 * @param customer
	 */
	public void printInfo(CustomerVO customer){
		System.out.println("이름 : " + customer.getName());
		System.out.println("전화번호 : " + customer.getPhoneNumber());
		System.out.println("대출금액 : " + customer.getLoanMoney());
		System.out.println("갚은 금액 :" + customer.getRepayMoney());
		System.out.println("담보 : " + customer.getDambo());
		System.out.println("대출시간 : " + customer.getLoanTime());
	}
	
	/**
	 * 해당고객정보가져오기
	 * @param name
	 * @return
	 */
	public CustomerVO getInfo( String name ) {
		CustomerVO selectedCustomer = new CustomerVO();
		for ( CustomerVO customer : this.customerList ) {
			if ( customer.getName().equals(name)){
				selectedCustomer = customer;
				return selectedCustomer;
			}
		}
		return new CustomerVO();
	}
	
}
