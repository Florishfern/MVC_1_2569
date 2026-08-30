# SUBMISSION - Exit Exam MVC 1/2569 (อาทิตย์เช้า)

## 1. วิธีเปิดโปรแกรม
- ภาษา/เฟรมเวิร์ก:  Java , Java Swing
- Entry point / คำสั่งเปิดโปรแกรม: 
  javac src/**/*.java src/*.java
  java -cp src Main
- หมายเหตุที่จำเป็น (ถ้ามี): 
  สามารถคอมไพล์และรันบนเครื่องใดก็ได้ที่มี JDK ติดตั้งอยู่
  ข้อมูลตั้งต้น seed_data.json ถูกแปลงและรันผ่านคลาส DataSeeder.java ตามเงื่อนไขที่โจทย์อนุญาต


## 2. ตารางเชื่อมโยง Requirements

| Requirement | Model / Domain | Controller / Action | View / Screen |
|---|---|---|---|
| R1 | ElectionModel, Candidate, Voter, Ballot |ElectionController ทำหน้าที่ประสานงานหลักระหว่าง Model และ View |MainFrame หน้าต่าง GUI หลักสำหรับสลับหน้าจอ |
| R2 |ElectionModel ตรวจสอบสิทธิ์ และสร้าง Ballot |handleVote() รับข้อมูลผู้สมัคร 3 ลำดับจาก View ไปอัปเดตใน Model|VotingView แสดงรายชื่อผู้สมัครและเมนูสำหรับเลือกโหวตอันดับ 1, 2, 3 |
| R3 |ตรวจหา Pattern บัตรซ้ำ >= 3 ใบ จัดกลุ่มและเปลี่ยน Status เป็นรอตรวจสอบ | handleCloseVoting() รับคำสั่งปิดโหวตจาก UI และสั่ง Model ให้ทำงาน| OfficerDashboardView หน้าจอเจ้าหน้าที่มีปุ่ม ปิดรับคะแนน|
| R4 |ElectionModel เปลี่ยน Status บัตรตามคำตัดสิน และคำนวณคะแนน| handleVerifyGroup() รับการตัดสิน จาก UI ส่งให้ Model|OfficerDashboardView เมนูจัดการกลุ่มบัตรรอตรวจสอบ และหน้าสรุปผลคะแนน |
| R5 |ซ่อนเงื่อนไข Validation ไว้ใน Model เมื่อทำผิดกฎ | Controller ดักจับ Error จาก Model แล้วสั่ง View ให้แสดงผล| VotingView/OfficerDashboardView แจ้งสาเหตุ Error และ Dashboard สรุปจำนวนบัตร|

## 3. ผลการทดสอบ

| กรณี | ผ่าน/ไม่ผ่าน | หมายเหตุ (เฉพาะที่จำเป็น) |
|---|---|---|
| T1 | ผ่าน | |
| T2 | ผ่าน | |
| T3 | ผ่าน | |
| T4 | ผ่าน | |
| T5 | ผ่าน | |
| T6 | ผ่าน | |

## 4. ความแตกต่างระหว่างแบบที่ออกกับโปรแกรมจริง (ถ้ามี)
ระบุไม่เกิน 3 ข้อ
1. ใน Class Diagram ตอนแรกไม่ได้ระบุคลาสสำหรับจัดการข้อมูล seed_data.json ไว้ แต่ตอนพัฒนาโปรแกรมจริงได้สร้างคลาส DataSeeder แยกออกมาต่างหาก เพื่อทำหน้าที่โหลดข้อมูลใส่ ElectionModel โดยเฉพาะ ทำให้โค้ดเป็นระเบียบและไม่ปะปนกับlogicหลัก
2. แอตทริบิวต์ score ในคลาส Candidate ตอนออกแบบตั้งใจจะให้ ElectionModel เป็นคนคำนวณคะแนน แต่ตอนเขียนโค้ดจริงพบว่าการเพิ่มแอตทริบิวต์ score และเมธอด addScore() เข้าไปในคลาส Candidate โดยตรงจะช่วยให้นำข้อมูลไปแสดงผลบน JTable ในหน้า View ได้ง่ายและสะดวกกว่ามาก
3. การใช้ Exception Handlingใน Class Diagram แบบรวมๆอาจจะไม่ได้วาดถึงคลาส Error ไว้ แต่ในการพัฒนาโปรแกรมจริงได้เลือกใช้กลไก throw new Exception() ในฝั่ง Model เมื่อมีการทำผิดกฎ  และใช้บล็อก try-catch ในฝั่ง Controller เพื่อจับ Error ไปแสดงผลเป็น Popup แจ้งเตือน

## 5. บันทึกการใช้ Generative AI
หากไม่ได้ใช้ ให้ระบุ **ไม่ได้ใช้ Generative AI**

| เวลาโดยประมาณ | เครื่องมือ | ใช้เพื่ออะไร | นำคำแนะนำไปใช้อย่างไร |
|---|---|---|---|
|10.00 | Gemini|ขอคำอธิบายแนวคิดพื้นฐานของการแบ่งไฟล์แบบ MVC (Model-View-Controller) ว่าควรจัดเก็บข้อมูลและ UI แยกกันอย่างไร |นำความเข้าใจที่ได้ มาใช้แบ่งคลาสของโปรแกรมออกเป็น Package model, view, และ controller อย่างชัดเจนตามที่โจทย์บังคับ |
|11.20 | Gemini |ขอคำแนะนำวิธีใช้ HashMap เบื้องต้นใน Java สำหรับการจัดกลุ่มข้อมูล Grouping |นำแนวคิดมาเขียนlogicในเมธอด closeVoting() เพื่อจัดกลุ่มบัตรที่มี Pattern เหมือนกันเข้าด้วยกัน ก่อนจะนับว่ากลุ่มไหนมีครบ 3 ใบ|
|11.45 | Gemini |สอบถามวิธีใช้คลาส JTabbedPane ใน Java Swing เพื่อใช้สลับหน้าจอไปมา |นำตัวอย่างโครงสร้างโค้ดการ Add Tab มาปรับใช้กับคลาส MainFrame เพื่อสลับระหว่าง VotingView และ OfficerDashboardView |
