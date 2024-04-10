import { createContext,useContext,useCallback,useMemo,useState } from "react";
import { DataStore } from "./StorageProvider";

var dbUser = await DataStore.getItem("user");
    
const UserContext = createContext(dbUser);

export function useUser() {
    const user = useContext(UserContext);
    return user;
}

export function UserProvider({ children }) {
    const [user, setUser] = useState(dbUser);

    const changeUser = useCallback((user) => {
        if (!user){
            throw Error("User cannot be empty!")
        }
        DataStore.setItem("user", user);
        setUser(user);
    }, []);

    const deleteUser = useCallback(() => {
        DataStore.removeItem("user");
        setUser(null)
    }, []);

    const value = useMemo(
        () => ({ user, changeUser,deleteUser }),
        [user, changeUser,deleteUser]
    );

    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    );
}