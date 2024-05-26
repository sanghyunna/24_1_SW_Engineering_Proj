"use client";

import "./globals.css";
import { createContext, useContext, useState } from "react";

interface AccountType {
	name: string;
	token: string;
	setAccount: (name: string, token: string) => void;
}

const AccountContext = createContext<AccountType | null>(null);
export const useAccountContext = () => useContext(AccountContext)!!;

export default function RootLayout({
	children,
}: Readonly<{
	children: React.ReactNode;
}>) {
	const [name, setName] = useState("");
	const [token, setToken] = useState("");

	const setAccount = (newName: string, newToken: string) => {
		setName(newName);
		setToken(newToken);
	};

	return (
		<html lang="en">
			<body>
				<AccountContext.Provider value={{ name, token, setAccount }}>
					{children}
				</AccountContext.Provider>
			</body>
		</html>
	);
}
