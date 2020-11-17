/**
 * 将byte字节数组转换为各种单位显示
 * @param byte
 */
export function switchByte(byte) {
    if (!byte)  return "";
    let num = 1024.00;
    if (byte < num)
        return byte + "B";
    if (byte < Math.pow(num, 2))
        return (byte / num).toFixed(2) + "KB";
    if (byte < Math.pow(num, 3))
        return (byte / Math.pow(num, 2)).toFixed(2) + "MB";
    if (byte < Math.pow(num, 4))
        return (byte / Math.pow(num, 3)).toFixed(2) + "G";
    return (byte / Math.pow(num, 4)).toFixed(2) + "T";
}
