-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 15, 2026 at 07:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `edusummarize_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `summaries`
--

CREATE TABLE `summaries` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_type` varchar(50) DEFAULT NULL,
  `course_name` varchar(100) DEFAULT NULL,
  `original_length` int(11) DEFAULT NULL,
  `summary_text` text DEFAULT NULL,
  `key_points` text DEFAULT NULL,
  `summary_length` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `summaries`
--

INSERT INTO `summaries` (`id`, `user_id`, `file_name`, `file_type`, `course_name`, `original_length`, `summary_text`, `key_points`, `summary_length`, `created_at`) VALUES
(10, 5, '1.Positive example.pdf', 'PDF', 'CSC577', 9295, 'Every computer system must have at least one operating system to run other programs. The OS helps you to communicate with the computer without knowing how to speak the computer\'s language. It is not possible for the user to use any computer or mobile device without having an operating system. Some computer processes are very lengthy and time-consuming. To speed the same process, a job with a similar type of needs are batched together and run as a group. In this type of OS, every user prepares the OS for use.', 'Instead, many common tasks, such as sending a network packet or displaying text on a standard output device, such as a display, can be offloaded to system software that serves as an intermediary between the applications and the hardware.|||This vastly reduces the amount of time and coding required to develop and debug an application, while ensuring tat users can control, configure and manage the system hardware through a common and well-understood interface.|||Without an operating system, every application would need to include its own UI, as well as the comprehensive code needed to handle all low-level functionality of the underlying computer, such as disk storage, network interfaces and so on.|||The present-day popular OS Windows first came to existence in 1985 when a GUI was created and paired with MS-DOS.|||Secondary-Storage Management: Systems have several levels of storage which includes primary storage, secondary storage, and cache storage.|||Applications like Browsers, MS Office, Notepad Games, etc., need some environment to run and perform its tasks.|||File management:- It manages all the file-related activities such as organization storage, retrieval, naming, sharing, and protection of files.|||In this type of OS, every user prepares his or her job on an offline device like a punch card and submit it to the computer operator.|||Security:- Security module protects the data and information of a computer system against malware threat and authorized access.|||Command interpretation: This module is interpreting commands given by the and acting system resources to process that commands.', NULL, '2025-12-25 12:58:55'),
(11, 5, '1.Positive example.pdf', 'PDF', NULL, 9295, 'Every computer system must have at least one operating system to run other programs. The OS helps you to communicate with the computer without knowing how to speak the computer\'s language. It is not possible for the user to use any computer or mobile device without having an operating system. Some computer processes are very lengthy and time-consuming. To speed the same process, a job with a similar type of needs are batched together and run as a group. In this type of OS, every user prepares the OS for use.', 'Instead, many common tasks, such as sending a network packet or displaying text on a standard output device, such as a display, can be offloaded to system software that serves as an intermediary between the applications and the hardware.|||This vastly reduces the amount of time and coding required to develop and debug an application, while ensuring tat users can control, configure and manage the system hardware through a common and well-understood interface.|||Without an operating system, every application would need to include its own UI, as well as the comprehensive code needed to handle all low-level functionality of the underlying computer, such as disk storage, network interfaces and so on.|||The present-day popular OS Windows first came to existence in 1985 when a GUI was created and paired with MS-DOS.|||Secondary-Storage Management: Systems have several levels of storage which includes primary storage, secondary storage, and cache storage.|||Applications like Browsers, MS Office, Notepad Games, etc., need some environment to run and perform its tasks.|||File management:- It manages all the file-related activities such as organization storage, retrieval, naming, sharing, and protection of files.|||In this type of OS, every user prepares his or her job on an offline device like a punch card and submit it to the computer operator.|||Security:- Security module protects the data and information of a computer system against malware threat and authorized access.|||Command interpretation: This module is interpreting commands given by the and acting system resources to process that commands.', NULL, '2025-12-25 13:05:07'),
(13, 5, '1.Positive example.pdf', 'PDF', 'Software Engineering', 9295, 'Every computer system must have at least one operating system to run other programs. The OS helps you to communicate with the computer without knowing how to speak the computer\'s language. It is not possible for the user to use any computer or mobile device without having an operating system. Some computer processes are very lengthy and time-consuming. To speed the same process, a job with a similar type of needs are batched together and run as a group. In this type of OS, every user prepares the OS for use.', 'Instead, many common tasks, such as sending a network packet or displaying text on a standard output device, such as a display, can be offloaded to system software that serves as an intermediary between the applications and the hardware.|||This vastly reduces the amount of time and coding required to develop and debug an application, while ensuring tat users can control, configure and manage the system hardware through a common and well-understood interface.|||Without an operating system, every application would need to include its own UI, as well as the comprehensive code needed to handle all low-level functionality of the underlying computer, such as disk storage, network interfaces and so on.|||The present-day popular OS Windows first came to existence in 1985 when a GUI was created and paired with MS-DOS.|||Secondary-Storage Management: Systems have several levels of storage which includes primary storage, secondary storage, and cache storage.|||Applications like Browsers, MS Office, Notepad Games, etc., need some environment to run and perform its tasks.|||File management:- It manages all the file-related activities such as organization storage, retrieval, naming, sharing, and protection of files.|||In this type of OS, every user prepares his or her job on an offline device like a punch card and submit it to the computer operator.|||Security:- Security module protects the data and information of a computer system against malware threat and authorized access.|||Command interpretation: This module is interpreting commands given by the and acting system resources to process that commands.', NULL, '2025-12-25 13:07:44'),
(15, 5, '1.Positive example.pdf', 'PDF', 'CSC584', 9295, 'Every computer system must have at least one operating system to run other programs. The OS helps you to communicate with the computer without knowing how to speak the computer\'s language. It is not possible for the user to use any computer or mobile device without having an operating system. Some computer processes are very lengthy and time-consuming. To speed the same process, a job with a similar type of needs are batched together and run as a group. In this type of OS, every user prepares the OS for use.', 'Instead, many common tasks, such as sending a network packet or displaying text on a standard output device, such as a display, can be offloaded to system software that serves as an intermediary between the applications and the hardware.|||This vastly reduces the amount of time and coding required to develop and debug an application, while ensuring tat users can control, configure and manage the system hardware through a common and well-understood interface.|||Without an operating system, every application would need to include its own UI, as well as the comprehensive code needed to handle all low-level functionality of the underlying computer, such as disk storage, network interfaces and so on.|||The present-day popular OS Windows first came to existence in 1985 when a GUI was created and paired with MS-DOS.|||Secondary-Storage Management: Systems have several levels of storage which includes primary storage, secondary storage, and cache storage.|||Applications like Browsers, MS Office, Notepad Games, etc., need some environment to run and perform its tasks.|||File management:- It manages all the file-related activities such as organization storage, retrieval, naming, sharing, and protection of files.|||In this type of OS, every user prepares his or her job on an offline device like a punch card and submit it to the computer operator.|||Security:- Security module protects the data and information of a computer system against malware threat and authorized access.|||Command interpretation: This module is interpreting commands given by the and acting system resources to process that commands.', NULL, '2025-12-25 13:08:29'),
(51, 13, 'Direct Text Input', 'TEXT', 'CSC577', 2331, 'Software Engineering is a discipline that applies engineering principles to the development, operation, and maintenance of software systems. It focuses on creating software that is reliable, efficient, and able to meet user requirements while remaining cost-effective and maintainable. Unlike basic programming, software engineering involves structured processes, proper documentation, teamwork, and quality control to manage the complexity of modern software projects. The main objective is to deliver high-quality software within a given time frame and budget while minimizing errors and risks. The SDLC typically consists of several phases, including requirement analysis, system design, implementation, testing, deployment, and Maintenance. The Waterfall model follows a linear sequence of phases and is suitable for projects with well-defined requirements. Agile development, on the other hand, emphasizes flexibility, customer collaboration, and iterative progress. Other models, such as the Spiral and V-Model, combine structured development with risk management and testing at every stage. Choosing the appropriate model helps ensure project success and ensures the software is reliable and reliable for future use, says the Software Development Life Cycle (SDLC) website. It also says that software engineering plays a crucial role in producing dependable software systems that support modern businesses, education, and everyday digital activities. It adds that quality assurance, testing and documentation help identify errors early, improve communication among team members, and ensure that the software can be easily maintained or upgraded in the future.', 'These practices help identify errors early, improve communication among team members, and ensure that the software can be easily maintained or upgraded in the future.|||The main objective is to deliver high-quality software within a given time frame and budget while minimizing errors and risks.|||It focuses on creating software that is reliable, efficient, and able to meet user requirements while remaining cost-effective and maintainable.|||Deployment releases the software to users, while maintenance involves fixing bugs, improving performance, and adding new features after release.|||Overall, software engineering plays a crucial role in producing dependable software systems that support modern businesses, education, and everyday digital activities.', NULL, '2026-01-10 15:58:42'),
(70, 17, 'Direct Text Input', 'TEXT', 'CSC541', 1472, 'Mobile programming focuses on building applications that are optimized for smaller screens, touch-based interaction, limited hardware resources, and varying network conditions. Unlike traditional desktop software, mobile applications are designed to be lightweight, responsive, and user-friendly to meet the needs of users who are often on the move. Native mobile development involves writing code specifically for a particular platform, such as Java or Kotlin for Android and Swift for iOS. Native applications usually offer better performance and deeper access to device features like the camera, GPS, sensors, and notifications. However, developing separate applications for each platform can require more time and resources. Mobile developers must consider screen size, orientation changes, touch gestures, and accessibility features when designing an application. Buttons, menus, and navigation must be easy to use with fingers rather than a mouse or keyboard. Performance is also important, as mobile devices have limited battery life and processing power compared to desktop computers. The goal of mobile programming is to provide a smooth and intuitive user experience for mobile devices such as smartphones, tablets, and iPods. Mobile programming can be done using different approaches.', 'Mobile programming focuses on building applications that are optimized for smaller screens, touch-based interaction, limited hardware resources, and varying network conditions.|||Mobile developers must consider screen size, orientation changes, touch gestures, and accessibility features when designing an application.|||Performance is also important, as mobile devices have limited battery life and processing power compared to desktop computers.|||Unlike traditional desktop software, mobile applications are designed to be lightweight, responsive, and user-friendly to meet the needs of users who are often on the move.|||Buttons, menus, and navigation must be easy to use with fingers rather than a mouse or keyboard.', NULL, '2026-01-14 10:36:54'),
(71, 17, '1. What is OS.pdf', 'PDF', NULL, 9295, 'Every computer system must have at least one operating system to run other programs. The OS helps you to communicate with the computer without knowing how to speak the computer\'s language. It is not possible for the user to use any computer or mobile device without having an operating system. Some computer processes are very lengthy and time-consuming. To speed the same process, a job with a similar type of needs are batched together and run as a group. In this type of OS, every user prepares the OS for use.', 'Instead, many common tasks, such as sending a network packet or displaying text on a standard output device, such as a display, can be offloaded to system software that serves as an intermediary between the applications and the hardware.|||This vastly reduces the amount of time and coding required to develop and debug an application, while ensuring tat users can control, configure and manage the system hardware through a common and well-understood interface.|||Without an operating system, every application would need to include its own UI, as well as the comprehensive code needed to handle all low-level functionality of the underlying computer, such as disk storage, network interfaces and so on.|||The present-day popular OS Windows first came to existence in 1985 when a GUI was created and paired with MS-DOS.|||Secondary-Storage Management: Systems have several levels of storage which includes primary storage, secondary storage, and cache storage.|||Applications like Browsers, MS Office, Notepad Games, etc., need some environment to run and perform its tasks.|||File management:- It manages all the file-related activities such as organization storage, retrieval, naming, sharing, and protection of files.|||In this type of OS, every user prepares his or her job on an offline device like a punch card and submit it to the computer operator.|||Security:- Security module protects the data and information of a computer system against malware threat and authorized access.|||Command interpretation: This module is interpreting commands given by the and acting system resources to process that commands.', NULL, '2026-01-14 11:18:06');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) DEFAULT 'User',
  `admin_level` int(11) DEFAULT 0,
  `is_premium` tinyint(1) NOT NULL DEFAULT 0,
  `pdf_count` int(11) NOT NULL DEFAULT 0,
  `account_status` tinyint(1) NOT NULL DEFAULT 1,
  `subscription_start` timestamp NULL DEFAULT NULL,
  `subscription_end` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `email`, `password`, `role`, `admin_level`, `is_premium`, `pdf_count`, `account_status`, `subscription_start`, `subscription_end`, `created_at`, `updated_at`) VALUES
(1, 'AdminL1', 'adminlevel1@gmail.com', 'level1', 'Admin', 1, 0, 0, 1, NULL, NULL, '2025-11-06 15:31:34', '2026-01-14 05:59:51'),
(3, 'Haikal ', 'Haikal124@gmail.com', 'QWERTY123', 'User', 0, 1, 15, 1, '2025-12-10 05:44:48', '2026-01-10 05:44:48', '2025-11-12 11:45:22', '2025-12-10 13:44:48'),
(4, 'AdminL3', 'admin@edusummarize.com', 'admin123', 'Admin', 3, 1, 0, 1, NULL, NULL, '2025-12-08 12:43:52', '2026-01-14 06:00:09'),
(5, 'Afiq', 'afiq@edu.com', 'AFIQ123', 'User', 0, 0, 12, 1, NULL, NULL, '2025-12-08 15:01:15', '2026-01-15 06:30:11'),
(11, 'Level2', 'adminlevel2@edusummarize.com', 'AdminLevel2', 'Admin', 2, 1, 0, 1, '2026-01-14 08:44:42', '2026-02-13 08:44:42', '2026-01-03 08:55:57', '2026-01-14 08:44:42'),
(13, 'Test User', 'newuser@edusummarize.com', 'Test123', 'User', 0, 1, 9, 1, '2026-01-10 08:02:48', '2026-02-10 08:02:48', '2026-01-04 09:55:43', '2026-01-10 16:02:48'),
(17, 'Demo', 'Demo@gmail.com', 'Demo12345', 'User', 0, 0, 15, 1, NULL, NULL, '2026-01-14 07:03:50', '2026-01-14 11:18:06');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `summaries`
--
ALTER TABLE `summaries`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_created_at` (`created_at`),
  ADD KEY `idx_course_name` (`course_name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_is_premium` (`is_premium`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `summaries`
--
ALTER TABLE `summaries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=72;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `summaries`
--
ALTER TABLE `summaries`
  ADD CONSTRAINT `summaries_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
