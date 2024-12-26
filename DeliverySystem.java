// 20241265 임성주 20241272 김수인
import java.util.Scanner;

// 배송 내역 기록을 위한 엔티티 클래스
class DeliveryHistoryEntity {
    private String deliveryDetails; // 배송 내역 설명
    private int quantity; // 배송 물품 수량

    // 배송 내역과 수량을 받아 초기화
    DeliveryHistoryEntity(String deliveryDetails, int quantity) {
        this.deliveryDetails = deliveryDetails;
        this.quantity = quantity;
    }

    // 배송 내역을 반환
    public String getDeliveryDetails() {
        return deliveryDetails;
    }

    // 물품 수량을 반환
    public int getQuantity() {
        return quantity;
    }
}

// 배송 관련 인터페이스: 물품 발송, 수령, 내역 출력 등을 정의
interface DeliveryManageable {
    void sendPackage(String packageName, int quantity); // 물품 발송 메서드
    void receivePackage(int quantity); // 물품 수령 메서드
    void printDeliveryHistory(); // 배송 내역 출력 메서드
    void printDeliveryInfo(); // 배송 정보 출력 메서드
}

// 배송을 관리하는 기본 클래스
abstract class Delivery implements DeliveryManageable {
    private String packageName; // 배송 물품 이름
    private String trackingNumber; // 배송 추적 번호
    private String recipientName; // 수취인 이름
    private int availablePackages; // 배송 가능한 물품 수량
    private int totalSentPackages; // 총 발송된 물품 수량
    private int totalReceivedPackages; // 총 수령된 물품 수량
    protected DeliveryHistoryEntity[] history; // 배송 내역 기록 배열
    protected int historyIndex; // 배송 내역 인덱스

    // 물품 정보 및 초기 배송 내역 배열 크기 설정
    Delivery(String packageName, String trackingNumber, String recipientName, int availablePackages) {
        this.packageName = packageName;
        this.trackingNumber = trackingNumber;
        this.recipientName = recipientName;
        this.availablePackages = availablePackages;
        this.history = new DeliveryHistoryEntity[100]; // 최대 100개의 배송 기록
        this.historyIndex = 0; // 배송 내역 시작 인덱스
    }

    // 물품 이름을 반환
    public String getPackageName() {
        return packageName;
    }

    // 추적 번호를 반환
    public String getTrackingNumber() {
        return trackingNumber;
    }

     // 수취인 이름을 반환
    public String getRecipientName() {
        return recipientName;
    }

    // 남은 물품 수량을 반환
    public int getAvailablePackages() {
        return availablePackages;
    }

    // 남은 물품 수량을 설정
    public void setAvailablePackages(int availablePackages) {
        this.availablePackages = availablePackages;
    }

    // 총 발송된 물품 수량을 반환
    public int getTotalSentPackages() {
        return totalSentPackages;
    }

     // 총 발송된 물품 수량을 설정
    public void setTotalSentPackages(int totalSentPackages) {
        this.totalSentPackages = totalSentPackages;
    }

    // 총 수령된 물품 수량을 반환
    public int getTotalReceivedPackages() {
        return totalReceivedPackages;
    }

    // 총 수령된 물품 수량을 설정
    public void setTotalReceivedPackages(int totalReceivedPackages) {
        this.totalReceivedPackages = totalReceivedPackages;
    }

    // 배송 정보 출력
    public void printDeliveryInfo() {
        System.out.println("배송 정보 출력 +++++++++++++++++++++++++");
        System.out.println("물품 이름 : " + packageName);
        System.out.println("수취인 이름 : " + recipientName);
        System.out.println("추적 번호 : " + trackingNumber);
        System.out.println("남은 물품 수량 : " + availablePackages);
        System.out.println("###################################################");
    }

    // 배송 내역을 출력
    public void printDeliveryHistory() {
        System.out.println("배송 내역 출력 ------------------------------------");
        for (int i = 0; i < historyIndex; i++) {
            System.out.printf("배송 내역(%d번째) : [%s] [%d개] \n",
                i + 1,
                history[i].getDeliveryDetails(),
                history[i].getQuantity());
        }
    }
}

// 발송 관리 클래스
class SendPackage extends Delivery {
    // 생성자: 물품 발송에 필요한 정보 설정
    SendPackage(String packageName, String trackingNumber, String recipientName, int availablePackages) {
        super(packageName, trackingNumber, recipientName, availablePackages);
    }

    // 물품 발송 처리
    @Override
    public void sendPackage(String packageName, int quantity) {
        // 발송 가능한 수량보다 많이 요청하면 오류 메시지 출력
        if (quantity > getAvailablePackages()) {
            System.out.println("-> 발송 가능한 물품 수량이 부족합니다.");
        } else {
            // 발송 처리가 가능하면 물품 수량 감소, 발송 수량 증가
            setAvailablePackages(getAvailablePackages() - quantity);
            setTotalSentPackages(getTotalSentPackages() + quantity);
            // 배송 내역에 발송 기록 추가
            super.history[super.historyIndex++] = new DeliveryHistoryEntity("발송 완료", quantity);
            System.out.println("-> " + quantity + "개의 물품을 발송하였습니다.");
        }
    }

    // 물품 수령은 불가능한 클래스임을 알림
    @Override
    public void receivePackage(int quantity) {
        System.out.println("-> 발송 처리만 가능한 클래스입니다.");
    }
}

// 수령 관리 클래스
class ReceivePackage extends Delivery {
    // 생성자: 물품 수령에 필요한 정보 설정
    ReceivePackage(String packageName, String trackingNumber, String recipientName, int availablePackages) {
        super(packageName, trackingNumber, recipientName, availablePackages);
    }

    // 물품 발송은 불가능한 클래스임을 알림
    @Override
    public void sendPackage(String packageName, int quantity) {
        System.out.println("-> 수령 처리만 가능한 클래스입니다.");
    }

    // 물품 수령 처리
    @Override
    public void receivePackage(int quantity) {
        // 수령 가능한 수량보다 많이 요청하면 오류 메시지 출력
        if (quantity > getAvailablePackages()) {
            System.out.println("-> 수령 가능한 물품 수량이 부족합니다.");
        } else {
            // 수령 처리가 가능하면 물품 수량 감소, 수령 수량 증가
            setAvailablePackages(getAvailablePackages() - quantity);
            setTotalReceivedPackages(getTotalReceivedPackages() + quantity);
            // 배송 내역에 수령 기록 추가
            super.history[super.historyIndex++] = new DeliveryHistoryEntity("수령 완료", quantity);
            System.out.println("-> " + quantity + "개의 물품을 수령하였습니다.");
        }
    }
}

// 배송 관리 시스템 클래스
class DeliveryManager {
    private Delivery[] deliveries; // 배송 객체 배열
    private int deliveryCount; // 배송 객체 개수

    // 생성자: 최대 배송 수와 배송 객체 배열 초기화
    DeliveryManager(int maxDeliveries) {
        this.deliveries = new Delivery[maxDeliveries];
        this.deliveryCount = 0;
    }

    // 발송 물품 객체 생성
    void createSendPackage(String packageName, String trackingNumber, String recipientName, int availablePackages) {
        deliveries[deliveryCount++] = new SendPackage(packageName, trackingNumber, recipientName, availablePackages);
        System.out.println("-> 발송 가능한 물품이 생성되었습니다!");
    }

     // 수령 물품 객체 생성
    void createReceivePackage(String packageName, String trackingNumber, String recipientName, int availablePackages) {
        deliveries[deliveryCount++] = new ReceivePackage(packageName, trackingNumber, recipientName, availablePackages);
        System.out.println("-> 수령 가능한 물품이 생성되었습니다!");
    }

    // 추적 번호로 배송 객체 검색
    int searchDelivery(String trackingNumber) {
        for (int i = 0; i < deliveryCount; i++) {
            if (deliveries[i].getTrackingNumber().equals(trackingNumber)) {
                return i; // 배송 객체 인덱스를 반환
            }
        }
        return -1; // 해당 추적 번호를 찾지 못하면 -1 반환
    }

    // 배송 정보를 출력하고 배송 내역도 출력
    void printDeliveryInfo(int index) {
        deliveries[index].printDeliveryInfo();
        deliveries[index].printDeliveryHistory();
    }

     // 물품 발송 또는 수령 처리
    void processDelivery(String trackingNumber, int quantity, boolean isSend) {
        int index = searchDelivery(trackingNumber);
        if (index != -1) {
            if (isSend) {
                deliveries[index].sendPackage(deliveries[index].getPackageName(), quantity); // 발송
            } else {
                deliveries[index].receivePackage(quantity); // 수령
            }
        } else {
            System.out.println("-> 추적 번호가 존재하지 않습니다!");
        }
    }

    // 사용자 메뉴 출력
    void printMenu() {
        System.out.println("1. 발송 물품 생성");
        System.out.println("2. 수령 물품 생성");
        System.out.println("3. 물품 발송");
        System.out.println("4. 물품 수령");
        System.out.println("0. 종료");
        System.out.print("입력 >> ");
    }
}

// 메인 클래스
public class DeliverySystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 사용자 입력
        DeliveryManager manager = new DeliveryManager(100); // 최대 100개의 배송 객체 관리
        int choice = -1; // 사용자의 메뉴 선택 변수

        // 사용자가 종료를 선택할 때까지 반복
        while (choice != 0) {
            manager.printMenu(); // 메뉴 출력
            choice = sc.nextInt(); // 사용자 입력 받기

            // 메뉴 선택에 따른 처리
            switch (choice) {
                case 1:
                    System.out.println("발송 물품 생성!");
                    System.out.print("물품 이름: ");
                    String packageName = sc.next();
                    System.out.print("추적 번호: ");
                    String trackingNumber = sc.next();
                    System.out.print("수취인 이름: ");
                    String recipientName = sc.next();
                    System.out.print("발송 가능한 수량: ");
                    int availablePackages = sc.nextInt();
                    manager.createSendPackage(packageName, trackingNumber, recipientName, availablePackages);
                    break;

                case 2:
                    System.out.println("수령 물품 생성!");
                    System.out.print("물품 이름: ");
                    packageName = sc.next();
                    System.out.print("추적 번호: ");
                    trackingNumber = sc.next();
                    System.out.print("수취인 이름: ");
                    recipientName = sc.next();
                    System.out.print("수령 가능한 수량: ");
                    availablePackages = sc.nextInt();
                    manager.createReceivePackage(packageName, trackingNumber, recipientName, availablePackages);
                    break;

                case 3:
                    System.out.println("물품 발송!");
                    System.out.print("물품 추적 번호: ");
                    trackingNumber = sc.next();
                    System.out.print("발송 수량: ");
                    int quantity = sc.nextInt();
                    manager.processDelivery(trackingNumber, quantity, true); // 발송
                    break;

                case 4:
                    System.out.println("물품 수령!");
                    System.out.print("물품 추적 번호: ");
                    trackingNumber = sc.next();
                    System.out.print("수령 수량: ");
                    quantity = sc.nextInt();
                    manager.processDelivery(trackingNumber, quantity, false); // 수령
                    break;

                case 0:
                    System.out.println("프로그램 종료");
                    break;

                default:
                    System.out.println("존재하지 않는 메뉴입니다!");
                    break;
            }
        }
    }
}

